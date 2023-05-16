package concesionarioimporter;

import concesionariomysql.Propietario;
import java.io.IOError;
import java.io.IOException;

/**
 *
 * @author xavi
 */
public class PropietarioCSVReaderStreams extends CSVReaderStreams<Propietario> {
    PropietarioCSVReaderStreams(String ppath) {
        super(ppath);
    }

    @Override
    public Propietario stringToObject(String line) throws IOException {
        if (line==null) return null;
        String[] fields=line.split(",");
        if (fields.length!=3) 
            throw new IOError(new Exception("O ficheiro de Propietarios está corrupto: "+line));
        return new Propietario(fields[0],fields[1],fields[2]); 
    }


}
