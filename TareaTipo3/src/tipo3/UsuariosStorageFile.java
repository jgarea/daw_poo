package tipo3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Set;
import java.util.TreeMap;

public final class UsuariosStorageFile implements Storage {
    private String filename;
    private String indexname;
    private TreeMap<String,Long> index;
    
    /**
     * Cando cargamos o índice o marquemos como "inválido" (neste caso o borramos).
     * Cando se pecha UsuariosStorageFile se debe gardar o índice de novo.
     * 
     * Si cando imos cargar un índice o índice é "inválido" (neste caso si non existe), o "reindexamos".
     * 
     * @param filename
     * @param indexname
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public UsuariosStorageFile(String filename,String indexname) throws Exception {
        this.index=new TreeMap<>();
        this.filename=filename;
        this.indexname=indexname;
        
        // Cargamos o index
        try(DataInputStream is=new DataInputStream(new FileInputStream(indexname))) {
            while(true) {
                String dni=is.readUTF();
                Long pos=is.readLong();
                index.put(dni,pos);
            }
        } catch (FileNotFoundException e) { 
            get();
        } catch(EOFException e) {       
            new File(indexname).delete();   // Unha vez cargamos o índice o borramos. Se garda de novo ao peche
        } 
    }
    
    private Usuario readUser(RandomAccessFile ras) throws EOFException, IOException {
        String dni,nome;
        int edade;
        dni=ras.readUTF();
        if (dni==null) throw new EOFException("End of file");
        if (dni.equals("*")) return null;   // Rexistro borrado
        nome=ras.readUTF();
        edade=ras.readInt();
        return new Usuario(dni,nome,edade);
    }
        
    /**
     * Retorna todos os usuarios.
     * Aproveitando que os ten que ler todos, reindexa a BBDD
     * @return
     * @throws Exception 
     */
    public Usuario[] get() throws Exception {
        TreeMap<String,Usuario> list=new TreeMap<>();   // Para telos ordeados por DNI
        Usuario user;
        Long pos;

        index=new TreeMap<>();
        try(RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            while(true) {
                pos=ras.getFilePointer();
                user=readUser(ras);
                if (user!=null) {
                    index.put(user.getDni(), pos);
                    list.put(user.getDni(),user);
                }
            }
        } catch(EOFException e) {}
        return list.values().toArray(new Usuario[0]);
    }
    
    @Override
    public Usuario get(String dni) throws Exception {
        Long pos=index.get(dni);
        if (pos==null) return null;
        try(RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            ras.seek(pos);
            return readUser(ras);
        }
    }

    @Override
    public void insert(Usuario user) throws Exception {
        Long pos=index.get(user.getDni());
        if (pos!=null) throw new Exception("O usuario xa existe"); // O ideal e deseñar unha StorageException
        try(RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            pos=ras.length();
            ras.seek(pos);
            ras.writeUTF(user.getDni());
            ras.writeUTF(user.getNome());
            ras.writeInt(user.getIdade());
            index.put(user.getDni(), pos);  // Actualizo o índice
        }
    }

    @Override
    public void update(Usuario user) throws Exception {
        Long pos=index.get(user.getDni());
        if (pos==null) throw new Exception("O usuario non existe"); // O ideal e deseñar unha StorageException
        try(RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            // "Elimino" o vello
            ras.seek(pos);
            ras.writeUTF("*");
            index.remove(user.getDni()); // Actualizo índice
            // Inserto o novo
            pos=ras.length();
            ras.seek(pos);
            ras.writeUTF(user.getDni());
            ras.writeUTF(user.getNome());
            ras.writeInt(user.getIdade());
            index.put(user.getDni(), pos); // Actualizo o índice
        }

    }

    @Override
    public void delete(String dni) throws Exception {
        Long pos=index.get(dni);
        if (pos==null) throw new Exception("O usuario non existe"); // O ideal e deseñar unha StorageException
        try(RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            // "Elimino" o vello
            ras.seek(pos);
            ras.writeUTF("*");
            index.remove(dni); // Actualizo índice
        }    
    }

    @Override
    public void close() throws Exception {
        try(DataOutputStream dos=new DataOutputStream(new FileOutputStream(indexname))) {
            Set<String> keys=index.keySet();
            for(String k:keys) {
                dos.writeUTF(k);
                dos.writeLong(index.get(k));
            }
        }
    }
}
