/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concesionarioimporter;

import concesionariomysql.Propietario;
import java.io.IOError;
import java.io.IOException;

/**
 *
 * @author xavi
 */
public class PropietarioCSVReader implements CSVReader<Propietario> {
    private LineReader reader;
    
    public PropietarioCSVReader(String filename) {
        reader=new LineReader(filename);
    }

    @Override
    public Propietario stringToObject(String line) throws IOException {
        if (line==null) return null;
        String[] fields=line.split(",");
        if (fields.length!=3) 
            throw new IOError(new Exception("O ficheiro de Propietarios está corrupto: "+line));
        return new Propietario(fields[0],fields[1],fields[2]);        
    }

    @Override
    public String getHeader() throws IOException {
        return reader.getHeader();
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
