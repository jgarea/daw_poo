package concesionariocsv;

import database.DatabaseException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


/**
 *
 * O que Vehiculo faga referencia ao número de liña donde está o propietario
 * en Propietarios.csv é unha faena. Sería moito mellor que fora o DNI, pero
 * o enunciado manda.
 * 
 * Para arranxar este problema almaceno nun ArrayList a correspondencia entre
 * número de líña e DNI e creo dous métodos para obter a liña a partir dun DNI e 
 * viceversa.
 */
public class PropietarioDatabaseOld extends CSVIndexedDatabase<String,Propietario>{
    private static ArrayList<String> lines;

    public PropietarioDatabaseOld(String filename) throws IOException {
        super(filename);
        makeLineIndex(filename);
    }

    // Relaciona número de liñas con DNI.
    private static void makeLineIndex(String filename) throws FileNotFoundException, IOException {
        lines=new ArrayList<>();
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            long position=ras.getFilePointer();
            ras.readLine();   // Header
            String line=ras.readLine();    // Data
            while(line!=null) {
                String[] fields=line.split(",");
                lines.add(fields[0]);   // DNI - Index 0 é a liña 1.
                line=ras.readLine();
            }
        }
    }
    
    // Retorna o propietario correspondente con nline. A primeira liña de datos é a 1
    public Propietario getPropietario(int nline) throws DatabaseException {
        try {
            String dni=lines.get(nline-1);
            if ((dni!=null)&&(!dni.startsWith("*"))) return get(dni); // Existe, e non  está borrrado.
            return null;
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }

    // Devolve o número de liña no que se atopa un DNI. A primeira liña é a 1
    public int getLine(String dni) {
        int idx=0;
        if (dni.startsWith("*")) return -1;
        while(idx<lines.size() && !lines.get(idx).equals(dni)) idx++;
        if (idx==lines.size()) return -1;
        return idx+1;
    }
    
    // Devolve a chave primaria do propietario (dni)
    @Override
    public String getKey(Propietario data) {
        return data.getDni();
    }

    // "Elimina" no ficheiro o propietario. Entendemos que esta eliminado si o seu DNI comeza con "*"
    @Override
    protected void remove(RandomAccessFile ras) throws DatabaseException {
        try {
            ras.write("*".getBytes()); // Escribo os Bytes para gardar a representación UTF-8 correctamente
        } catch (IOException ex) {
            throw new DatabaseException("Error Propietario remove: "+ex.getMessage());
        }
    }
    
    // Garda o propietario no ficheiro.
    @Override
    protected long write(RandomAccessFile ras, Propietario data) throws DatabaseException {
        long ret=super.write(ras, data);
        lines.add(data.getDni());  // Mapeamos o DNI ao número de liña
        return ret;
    }
  
    // Retorna o Propietario representado pola liña de CSV (String)
    @Override
    protected Propietario stringToObject(String line) throws DatabaseException {
        String[] fields=line.split(",");
        if (fields.length!=3) 
            throw new DatabaseException("O ficheiro de Propietarios está corrupto: "+line);
        return new Propietario(fields[0],fields[1],fields[2]);         
    }

    // Retorna un String (liña CSV) que representa o obxecto Propietario
    @Override
    protected String getCSVLine(Propietario data) throws DatabaseException {
        return data.getDni()+","+data.getNome()+","+data.getApelidos();
    }

    // Indica si está borrado. Entendemos que un propietario está "borrado" si o seu
    // dni comeza con *
    @Override
    protected boolean isDeleted(Propietario data) {
        return data.getDni().startsWith("*");
    }
    
}
