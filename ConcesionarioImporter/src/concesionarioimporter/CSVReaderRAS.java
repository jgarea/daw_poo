package concesionarioimporter;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public abstract class CSVReaderRAS<R> implements CSVReader<R>{
    protected String filename;
    protected String header=null;
    String[] cache;
        
    protected CSVReaderRAS(String filename) {
        this.filename=filename;
    }
    
    public String[] readCSV() throws FileNotFoundException, IOException {
        String line;
        ArrayList<String> list=new ArrayList<>();
        try(RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
           line=readLine(ras); 
           header=line;
           while(line!=null) {
                list.add(line);
                line=readLine(ras);
           }
        }
        cache=list.toArray(String[]::new);
        return cache;
    }
    
    @Override
    public String getHeader() {
        return header;
    }
    
    @Override
    public String getLine(int idx) throws IOException {
        if (cache==null) readCSV();
        if (idx<cache.length) return cache[idx];
        return null;
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
   
    public abstract R stringToObject(String line) throws IOException;
}
