package piscina;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author xavi
 */
public class SocioStreamsDatabase implements Database<Socio> {
    private final HashMap<String,Socio> data=new HashMap<>();

    public SocioStreamsDatabase() throws Exception {
        try(BufferedReader rdr=new BufferedReader(new FileReader("socios.dat"))) {
            String info=rdr.readLine();
            while(info!=null) {
                Socio s=new Socio();
                s.fromText(info);
                data.put(s.getDni(),s);
                info=rdr.readLine();
            }
        } catch(FileNotFoundException e) {
            System.out.println("Sen datos.");
        }
    }

    @Override
    public void close() throws Exception {
        try(PrintWriter pw=new PrintWriter("socios.dat")) {
            for(Socio s:data.values()) {
                pw.println(s.toText());
            }
        }
    }

    @Override
    public Iterator iterator() {
        return data.values().iterator();
    }

    @Override
    public void save(Socio s) throws DatabaseException {
        if (data.get(s.getDni())!=null) throw new DatabaseException("O Socio xa existe");
        data.put(s.getDni(), s);
    }

    @Override
    public void update(Socio s) throws DatabaseException {
        if (data.get(s.getDni())==null) throw new DatabaseException("O Socio non existe");
        data.put(s.getDni(), s);   
    }

    @Override
    public void delete(Socio s) throws DatabaseException {
        if (data.get(s.getDni())==null) throw new DatabaseException("O Socio non existe");
        data.remove(s.getDni());
    }

    @Override
    public Socio get(Socio s) throws DatabaseException {
        return data.get(s.getDni());
    }
    
}
