package com.prog11.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Realizar la conexión contra la base de datos y el cierre de la misma. Para
 * ello implementará los métodos openConnection() y closeConnection().
 *
 * @author Juan
 */
public class ConnectionDB {

    private Connection con = null;

    /**
     * Devuelve la conexión a la base de datos
     * @return conexion a la base de datos
     */
    public Connection openConnection() {
        if (con == null) {
            try {
                String url = "jdbc:mariadb://localhost/concesionario";
                con = DriverManager.getConnection(url, "root", "");
                System.out.println("Conexion Exitosa");
            } catch (SQLException ex) {
                System.out.println("SQLException" + ex.getMessage());
                System.out.println("SQLState" + ex.getSQLState());
                System.out.println("ErrorCode" + ex.getErrorCode());
            }
        }
        return con;
    }

    /**
     * Cierra la conexión con la base de datos
     */
    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        con = null;
    }

}
