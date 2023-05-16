package concesionarioimporter;

import concesionariomysql.Vehiculo;
import java.io.IOError;
import java.io.IOException;


public class VehiculoCSVReader implements CSVReader<Vehiculo>{
    private LineReader reader;

    public VehiculoCSVReader(String filename) {
        reader=new LineReader(filename);
    }
    
    @Override
    public String getHeader() throws IOException {
        return reader.getHeader();
    }

    @Override
    public Vehiculo stringToObject(String line) throws IOException {
        if (line==null) return null;
        String[] fields=line.split(",");
        if (fields.length<3 || fields.length>4) 
            throw new IOError(new Exception("O ficheiro de Vehículos está corrupto: "+line));
        return new Vehiculo(fields[0],fields[1],fields[2]);       
    }

    @Override
    public String[] readCSV() throws IOException {
        return reader.readAll();
    }

    @Override
    public String getLine(int idx) throws IOException {
        return reader.getLine(idx);
    }
}
