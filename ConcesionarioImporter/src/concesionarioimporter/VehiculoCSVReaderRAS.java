package concesionarioimporter;

import concesionariomysql.Vehiculo;
import java.io.IOError;
import java.io.IOException;

public class VehiculoCSVReaderRAS extends CSVReaderRAS<Vehiculo> {
    VehiculoCSVReaderRAS(String vpath) {
        super(vpath);
    }

    @Override
    public Vehiculo stringToObject(String line) throws IOException {
        if (line==null) return null;
        String[] fields=line.split(",");
        if (fields.length<3 || fields.length>4) 
            throw new IOError(new Exception("O ficheiro de Vehículos está corrupto: "+line));
        return new Vehiculo(fields[0],fields[1],fields[2]);            
    }
}
