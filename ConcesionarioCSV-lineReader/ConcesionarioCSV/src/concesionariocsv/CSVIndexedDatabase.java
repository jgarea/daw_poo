package concesionariocsv;

import database.DatabaseException;
import database.Index;
import database.IndexedDatabase;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Collection;

public abstract class CSVIndexedDatabase<K,R> extends IndexedDatabase<K,R> {
    private String header;
    
    public CSVIndexedDatabase(String filename) {
        super(filename);
    }
        
    /**
     * Sobrepoñemos makeIndex de IndexedDatabase porque temos que saltar a 
     * cabeceira do CSV, que non se indexa
     */
    @Override
    protected void makeIndex() throws DatabaseException {
        R data;
        // try-with-resources: Pecha ao rematar
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            index=new Index<>();
            header=readLine(ras); // Lemos a cabeceira.
            long position=ras.getFilePointer();
            data=read(ras);
            while(data!=null) {
                // Si o elemento está borrado, non o poño no índice
                if (!isDeleted(data)) index.addIndex(getKey(data), position);
                position=ras.getFilePointer();
                data=read(ras);
            }
        } catch (Exception ex) {
            index=null;
            throw new DatabaseException("Indexed Database Constructor Error: "+ex.getMessage());
        }
    }
    
    /**
     * Temos que sobrepoñer pack() de IndexedDatabase porque temos que copiar a cabeceira no destiño
     */
    public void pack() throws DatabaseException {
        String temp="/tmp/"+filename.substring(filename.lastIndexOf("/"));
        R r;
        
        if (index==null) makeIndex();
        File orig=new File(filename);
        File dest=new File(temp);
        try (RandomAccessFile fras=new RandomAccessFile(orig,"r");
             RandomAccessFile fdest=new RandomAccessFile(dest,"rws")) {
            String header=fras.readLine();  // Copiamos a cabeceira
            fdest.write(header.getBytes());
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
     * Lee unha liña do CSV e a transforma a obxecto mediante o método abstracto stringToObject
     */
    @Override
    protected R read(RandomAccessFile ras) throws DatabaseException {
        Propietario propietario=null;
        String line="";
        String[] fields=null;
        try {
            line=readLine(ras);
            // Si devolve null, pero non temos liña en blanco, nos aseguramos
            // De que imos ter ao final unha liña en blanco para que as altas
            // non se fagan detrás da última liña.
            if (line==null) return null;
            line=line.trim();
            
            // Si temos unha liña en blanco, é que non temos máis datos
            if (line.length()==0) return null;
            return stringToObject(line);
        } catch(NumberFormatException e) {
            throw new DatabaseException("O propietario do vehículo non é válido: "+line);
        } catch (IOException ex) {
            throw new DatabaseException("ERRO lendo Vehiculo");
        } catch (IndexOutOfBoundsException e) {
            throw new IOError(new DatabaseException("O ficheiro de Vehiculos está corrupto: "+line));
        }
    }

    /**
     * Escribe o rexistro data no CSV pasando os datos a unha liña String de CSV
     * mediante o método abstracto getCSVLine
     */
    @Override
    protected long write(RandomAccessFile ras, R data) throws DatabaseException {
        String vline=getCSVLine(data);
        
        try {
            long position=ras.getFilePointer();
            if (ras.getFilePointer()==ras.length()) {
                ras.write("\n".getBytes());
                position=ras.getFilePointer();
            }
            else vline+="\n";
            ras.write(vline.getBytes());
            return position;
        } catch (IOException ex) {
           throw new DatabaseException("write Vehiculo: "+ex.getMessage());
        }
    }
    
    /**
     * Borra o CSV deixando so a cabeceira
     * @throws FileNotFoundException
     * @throws IOException 
     */
    protected void clear() throws FileNotFoundException, IOException {
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            ras.setLength(0);
            ras.write(header.getBytes());
            index=null;
        }
    }
    
    /**
     * Lee unha Liña do ficheiro CSV respetando a codificación UTF-8
     * readLine de RandomAccessFile non serve, porque usa 2 bytes por caracter en todos os casos
     * e readUTF tampouco porque se gardaría o número de bytes do String antes de cada liña.
     */
    protected String readLine(RandomAccessFile ras) throws IOException {
        ArrayList<Byte> line=new ArrayList<>();
        try {
            byte cbyte=ras.readByte();
            while(cbyte!='\n') {
                line.add(cbyte);
                cbyte=ras.readByte();
            }
        } catch(EOFException e) {
        }
        int size=line.size();
        if (size==0) return null;
        byte[] chars=new byte[line.size()];
        for(int idx=0;idx<size;idx++) chars[idx]=line.get(idx);
        return new String(chars);
    }
    
    protected abstract String getCSVLine(R data) throws DatabaseException;
    protected abstract R stringToObject(String line) throws DatabaseException;
}
