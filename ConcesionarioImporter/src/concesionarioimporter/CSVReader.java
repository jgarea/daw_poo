/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package concesionarioimporter;

import concesionariomysql.Propietario;
import java.io.IOException;

/**
 *
 * @author xavi
 */
public interface CSVReader<R> {
    public String getHeader() throws IOException;
    public String[] readCSV() throws IOException;
    public String getLine(int idx) throws IOException;
    
    public default R getObject(int idx) throws IOException  {
        return stringToObject(getLine(idx));
    }
    
    public abstract R stringToObject(String line) throws IOException;
}
