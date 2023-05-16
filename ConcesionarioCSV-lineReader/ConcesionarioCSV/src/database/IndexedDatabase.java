package database;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class IndexedDatabase<K,R> implements Database<R> {
    protected Index<K> index=null;
    protected String filename;
    
    public IndexedDatabase(String filename) {
        this.filename=filename;
    }
    
    public String getFilename() {
        return filename;
    }
    
    /**
     * Crea o índice:
     *  - Abre o RandomAccessFile
     *  - Crea o obxecto Index
     *  - Repite
     *      - Averiguar a posición actual no ficheiro
     *      - Ler o rexistro
     *      - Gardar no índice a chave do rexistro e a posición na que se leeu o rexistro
     *  Mentres teñamos rexistros que ler.
     * @throws DatabaseException 
     */
    protected void makeIndex() throws DatabaseException {
        R data;
        // try-with-resources: Pecha ao rematar
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            index=new Index<>();
            long position=ras.getFilePointer();
            data=read(ras);
            while(data!=null) {
                // Si o elemento está borrado, non o poño no índice
                if (!isDeleted(data)) index.addIndex(getKey(data), position);
                position=ras.getFilePointer();
                data=read(ras);
            }
        } catch (IOException ex) {
            index=null;
            throw new DatabaseException("Indexed Database Constructor Error: "+ex.getMessage());
        }
    }
    
    /**
     *      - Si non temos índice, o creamos
     *      - Miramos no índice si o rexistro xa existe, si existe lanzamos o erro
     *      - Abrimos o RandomAccesFile
     *      - Nos colocamos ao final e gardamos a posición
     *      - Escribimos os datos
     *      - Engadimos a posición dos novos datos ao índice
     */
    @Override
    public void insert(R data) throws DatabaseException {
        if (index==null) makeIndex();
        Long position=index.getPosition(getKey(data));
        if (position!=null) throw new DatabaseException("Insert: O rexistro xa existe");
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            position=ras.length();
            ras.seek(position);
            position=write(ras,data);
            index.addIndex(getKey(data),position);
        } catch (Exception ex) {
            throw new DatabaseException("Indexed Database insert: "+ex.getMessage());
        }
    }

    /**
     *      - Si non temos índice, o creamos
     *      - Miramos no índice si o rexistro xa existe, si non existe lanzamos o erro
     *      - Abrimos o RandomAccesFile
     *      - Nos colocamos na posición do rexistro e o marcamos como eliminado
     *      - Eliminamos o índice correspondente
     *      - Engadimos o novo rexistro ao final
     */
    @Override
    public void update(R data) throws DatabaseException {
        if (index==null) makeIndex();
        K key=getKey(data);
        Long position=index.getPosition(key);
        if (position==null) throw new DatabaseException("Update: O rexistro non existe");
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            ras.seek(position);
            remove(ras);
            index.delIndex(key);

            position=ras.length();
            ras.seek(position);
            position=write(ras,data);
            index.addIndex(key,position);
        } catch (Exception ex) {
            throw new DatabaseException("Indexed Database update: "+ex.getMessage());
        }
    }

    /**
     *      - Si non temos índice, o creamos
     *      - Miramos no índice a posición do rexistro
     *      - Abrimos o RandomAccesFile
     *      - Si o rexistro existe:
     *           - Nos colocamos na posición do rexistro e o marcamos como eliminado
     *           - Eliminamos o índice correspondente
     *      - Engadimos o novo rexistro ao final
     */
    @Override
    public void insertOrUpdate(R data) throws DatabaseException {
        if (index==null) makeIndex();
        K key=getKey(data);
        Long position=index.getPosition(key);
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            if (position!=null) {
                ras.seek(position);
                remove(ras);
                index.delIndex(key);
            }
            
            position=ras.length();
            ras.seek(position);
            position=write(ras,data);
            index.addIndex(getKey(data),position);
            
        } catch (Exception ex) {
            throw new DatabaseException("Indexed Database insertOrUpdate: "+ex.getMessage());
        }    
    }

    /**
     *      - Si non temos índice, o creamos
     *      - Miramos no índice si o rexistro xa existe, si non existe lanzamos o erro
     *      - Abrimos o RandomAccesFile
     *      - Nos colocamos na posición do rexistro e o marcamos como eliminado
     *      - Eliminamos o índice correspondente
     */
    @Override
    public void delete(R data) throws DatabaseException {
        if (index==null) makeIndex();
        K key=getKey(data);
        Long position=index.getPosition(key);
        if (position==null) throw new DatabaseException("Delete: O rexistro non existe");
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            ras.seek(position);
            remove(ras);
            index.delIndex(key);
        } catch (Exception ex) {
            throw new DatabaseException("Indexed Database delete: "+ex.getMessage());
        }
    }
    
    /***
     * En lugar de procesar todo o ficheiro secuencialemnte, percorremos
     * o índice, xa que os elementos borrados non están no índice.
     * 
     * - Si non temos índice, o creamos
     * - Abrimos o RandomAccesFile
     * - Recuperamos do índice as posicións dos datos
     * - Percorremos as posicións:
     *      Nos posicionamos e lemos o dato
     *      Si o dato cumple o criterio: 
     *          Almacenamos o dato nun ArrayList
     *          Retrocedemos ao inicio do dato
     *          Marcamos como eliminado e o quitamos do indice
     * - Transformamos o ArrayList nun Array e o retornamos
     * @param c
     * @return
     * @throws DatabaseException 
     */
    @Override
    public R[] delete(Criteria<R> c) throws DatabaseException {
        ArrayList<R> deleted=new ArrayList<>();
        R data;
        if (index==null) makeIndex();
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            Collection<Long> records=index.getPositions();
            // Usamos un Iterator, para poder eliminar elementos
            // mentres iteramos.
            Iterator<Long> walk=records.iterator();
            while(walk.hasNext()) {
                Long pos=walk.next();
                ras.seek(pos);
                data=read(ras);
                if (c.accept(data)) {
                    deleted.add(data);
                    ras.seek(pos);
                    remove(ras);
                    walk.remove();
                    //index.delIndex(getKey(data));
                }
            }
        } catch (Exception ex) {
            throw new DatabaseException("Indexed Database delete criteria: "+ex.getMessage());
        }
        if (deleted.isEmpty()) return null;
        return (R[])deleted.toArray();
    } 

    
    /**
     * Si percorremos o índice, non precisamos ler os borrados.
     * 
     * - Si non temos índice, o creamos
     * - Abrimos o RandomAccesFile
     * - Recuperamos do índice as posicións dos datos
     * - Percorremos as posicións:
     *      Nos posicionamos e lemos o dato
     *      Si o dato cumple o criterio: 
     *          Almacenamos o dato nun ArrayList
     * - Transformamos o ArrayList nun Array e o retornamos
     * 
     * @param c
     * @return
     * @throws DatabaseException 
     */
    @Override
    public R[] select(Criteria<R> c) throws DatabaseException {
        ArrayList<R> selected=new ArrayList<>();
        R data;
        
        if (index==null) makeIndex();
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            Collection<Long> records=index.getPositions();
            for(Long pos:records) {
                ras.seek(pos);
                data=read(ras);
                if (data!=null && c.accept(data)) selected.add(data);
            }
        } catch (Exception ex) {
            throw new DatabaseException("Indexed Database select criteria: "+ex.getMessage());
        }
        if (selected.isEmpty()) return null;
        return selected.toArray((R[])Array.newInstance(selected.get(0).getClass(), 0));
    }
    
    /**
     * Retorna o rexistro correspondente coa chave key
     * @param key Chave primaria a consultar
     * @return Rexistro ou null si non existe
     * @throws DatabaseException 
     * 
     * Retorna null si o rexistro non existe
     */
    public R get(K key) throws DatabaseException {
        R data=null;
        if (index==null) makeIndex();
        Long position=index.getPosition(key);
        if (position!=null)  {
            // try-with-resources: Pecha ao rematar
            try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
                ras.seek(position);
                data=read(ras);
            } catch (Exception ex) {
                throw new DatabaseException("Indexed Database get: "+ex.getMessage());
            }
        }
        return data;
    }
    
    /**
     * Reescribe o ficheiro eliminando os rexistros borrados
     * @throws DatabaseException 
     */
    public void pack() throws DatabaseException {
        String temp="/tmp/"+filename.substring(filename.lastIndexOf("/"));
        R r;
        
        if (index==null) makeIndex();
        File orig=new File(filename);
        File dest=new File(temp);
        try (RandomAccessFile fras=new RandomAccessFile(orig,"r");
             RandomAccessFile fdest=new RandomAccessFile(dest,"rws")) {

            Collection<Long> positions=index.getPositions();
            for(Long p:positions) {
                fras.seek(p);
                r=read(fras);
                if (isDeleted(r)) 
                    throw new DatabaseException("Erro de consistencia. Rexistro marcado como borrado "+r);
                write(fdest,r);
            }
            Files.move(dest.toPath(),orig.toPath(),REPLACE_EXISTING);
            
        } catch (Exception ex) {
            throw new DatabaseException("Indexed Database delete: "+ex.getMessage());
        }
    }
    
    /**
     * Retorna o valor da chave primaria dos datos.
     * @param data Datos
     * @return valor da chave primaria
     * 
     * Este método non se pode implementar porque o valor da chave primaria depende da estrutura
     * concreta dos datos (os atributos da clase)
     */
    public abstract K getKey(R data);
    
    /**
     * Marca o rexistro almacenado na posición actual como eliminado físicamente. 
     * @throws DatabaseException 
     * 
     * Este método non se pode implementar porque o xeito de marcar como borrado un rexistro
     * depende da estrutura dos datos almacenados (atributos). E posible por exemplo que un *
     * no inicio dos datos non represente un rexistro borrado para algún tipo de información.
     */
    protected abstract void remove(RandomAccessFile ras) throws DatabaseException;
    
    /**
     * Lee o rexistro do ficheiro da posición actual e o garda en object
     * @return Datos leidos ou null si estamos no final do ficheiro
     * @throws DatabaseException Si se produce un erro
     * 
     * Este método non se pode implementar porque o xeito de ler a información depende
     * da estrutura (dos atributos) da clase R
     */
    protected abstract R read(RandomAccessFile ras) throws DatabaseException;
    
    /**
     * Garda o rexistro no ficheiro na posición actual
     * @param data Información a gardar
     * @throws DatabaseException 
     * 
     * Este método non se pode implementar porque o xeito de almacenar a información depende
     * da estrutura (dos atributos) da clase R
     */
    protected abstract long write(RandomAccessFile ras,R data) throws DatabaseException;
    
    /**
     * Indica si data está borrado (true) ou son datos válidos (false)
     * @param data
     * @return true ou false
     * 
     * Este método non se pode implementar porque depende do xeito en que se marquen 
     * como borrados os rexistros.
     */
    protected abstract boolean isDeleted(R data);
    
}
