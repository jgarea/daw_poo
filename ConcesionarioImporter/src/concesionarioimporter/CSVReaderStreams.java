package concesionarioimporter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class CSVReaderStreams<R> implements CSVReader<R> {
    protected String filename;
    protected String header=null;
    protected String[] cache;
        
    protected CSVReaderStreams(String filename) {
        this.filename=filename;
    }
    
    public String[] readCSV() throws FileNotFoundException, IOException {
        ArrayList<String> list=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(filename))) {
           String line=reader.readLine(); 
           header=line;
           while(line!=null) {
                list.add(line);
                line=reader.readLine();
           }
        }
        cache=list.toArray(String[]::new);
        return cache;
    }
    
    @Override
    public String getLine(int idx) throws IOException {
        if (cache==null) readCSV();
        if (idx<cache.length) return cache[idx];
        return null;
    }
    
    public String getHeader() {
        return header;
    }
   
    public abstract R stringToObject(String line) throws IOException;
}
