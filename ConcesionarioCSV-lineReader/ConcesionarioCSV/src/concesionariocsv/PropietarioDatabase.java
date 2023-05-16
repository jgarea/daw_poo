package concesionariocsv;

import database.DatabaseException;
import database.LineReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

/**
 *
 * O que Vehiculo faga referencia ao número de liña donde está o propietario
 * en Propietarios.csv é unha faena. Sería moito mellor que fora o DNI, pero
 * o enunciado manda.
 * 
 * Para arranxar este problema almaceno nun ArrayList a correspondencia entre
 * número de líña e DNI e creo dous métodos para obter a liña a partir dun DNI e 
 * viceversa.
 * 
 * Esta versión en lugar de facer ese ArrayList emprega unha clase feita no paquete database
 * chamada LineReader que nos permite acceder a un ficheiro de texto por liñas.
 * 
 */
public class PropietarioDatabase extends CSVIndexedDatabase<String,Propietario>{
    private LineReader reader;
    
    public PropietarioDatabase(String filename) throws IOException {
        super(filename);
        reader=new LineReader(filename);
    }
   
    // Retorna o propietario correspondente con nline. A primeira liña de datos é a 1
    public Propietario getPropietario(int nline) throws DatabaseException {
        
        try {
            String line=reader.getLine(nline);
            if ((line !=null)&&(!line.startsWith("*"))) return stringToObject(line); // Existe, e non  está borrrado.
            return null;
        } catch(IndexOutOfBoundsException | IOException e) {
            return null;
        }
    }

    // Devolve o número de liña no que se atopa un DNI. A primeira liña é a 1
    // A desvantaxe respecto a outra versión é que temos que percorrer todas as liñas ata
    // atopar o DNI buscado....
    public int getLine(String dni) throws DatabaseException {
        Iterator<String> it=reader.iterator();
        if (it!=null) {
            int idx=1;
            while(it.hasNext()) {
                Propietario p=stringToObject(it.next());
                if (p.getDni().equals(dni)) return idx;
                idx++;
            }
        }
        return -1;
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
        try {
            long ret=super.write(ras, data);
            reader.refresh();
            return ret;
        } catch (IOException ex) {
            throw new DatabaseException(ex.getMessage());
        }
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
