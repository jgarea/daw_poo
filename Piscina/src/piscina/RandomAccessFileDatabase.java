/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piscina;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author xavi
 */
public abstract class RandomAccessFileDatabase<T extends Textualizable> implements Database<T> {
    private final RandomAccessFile file;
    private int numrecords=0;
    private long lastposition=0;
    
    public RandomAccessFileDatabase() throws FileNotFoundException, IOException {
        file=new RandomAccessFile("ras_socios.dat","rw");
        try {
            numrecords=file.readInt();
        } catch(EOFException e) {
            file.writeInt(0);
        }
        lastposition=0;
    }

    @Override
    public void save(T data) throws DatabaseException {
        try {
            T s=get(data);
            if (s!=null) throw new DatabaseException("O Socio xa existe");
            file.seek(file.length());
            file.writeByte(0);
            file.writeUTF(data.toText());
            numrecords++;
            file.seek(0);
            file.writeInt(numrecords);
        } catch (IOException ex) {
            throw new DatabaseException(ex.getMessage());
        }    
    }

    @Override
    public void update(T data) throws DatabaseException {
        try {
            T s=get(data);
            if (s==null) throw new DatabaseException("O Socio non existe");
            file.seek(lastposition+4);
            file.writeUTF(data.toText());
        } catch (IOException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    @Override
    public void delete(T data) throws DatabaseException {
        try {
            T s=get(data);
            if (s==null) throw new DatabaseException("O Socio non existe");
            file.seek(lastposition);
            file.writeByte(1);
            file.seek(0); //1 borrado
            numrecords--;
            file.writeInt(numrecords);
        } catch (IOException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    @Override
    public T get(T data) throws DatabaseException {
        for(T s:this) {
            if (s.equals(data)) return s;
        }
        return null;
        /*try {
            String dnisearch=data.getDni();
            if (dnisearch==null) return null;
            file.seek(0l);
            while(true) {
                Byte baixa=file.readByte();
                String info=file.readUTF();
                data.fromText(info);
                if (data.getDni().equals(dnisearch)) return data;
            }
        } catch(EOFException ex) {
            data=null;
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
        return data;
        */
    }

    @Override
    public void close() throws Exception {
        file.close();
    }

    @Override
    public Iterator<T> iterator() {
            return new Iterator<T>() {
                T s;
                int pos=0;
                
                @Override
                public boolean hasNext() {
                    return pos<numrecords;
                }
                
                @Override
                public T next() {
                    try {
                        Byte baixa;
                        String info;
                        
                        if (pos==0) file.seek(4); // Posicion 1 rexistro
                        s=buildObject();
                        do {
                            lastposition=file.getFilePointer();
                            baixa=file.readByte();
                            info=file.readUTF();
                        } while(baixa==1);  // 1 indica borrado
                        s.fromText(info);
                        pos++;
                        return s;
                    } catch (EOFException ex) {
                        throw new NoSuchElementException();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex.getMessage());
                    }
                }
            };
    }
    
    protected abstract T buildObject();
}
