package com.prog11.bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan
 */
public class PropietariosDAO {

    /**
     * Insertar un nuevo propietario: Recibe por parámetro los datos de un
     * propietario a insertar, trata de insertarlo en la BBDD y devuelve 0 si la
     * operación se realizó con éxito o -1 en caso contrario.
     *
     * @param con
     * @param prop
     * @return
     */
    public static int addPropietario(Connection con, Propietario prop) {
        String sql = "INSERT INTO propietarios (id_prop, nombre_prop, dni_prop) VALUES (?,?,?);";
        int result = -1;
        try ( PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, prop.getId_prop());
            pstm.setString(2, prop.getNombre_prop());
            pstm.setString(3, prop.getDni_prop());

            result = pstm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PropietariosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (result > 0) {
            return 0;
        }
        return -1;
    }

    /**
     * Recibe por parámetro el DNI de un propietario y devuelve una lista con la
     * matrícula, marca, número de kms y precio de todo sus vehículos.Retornará
     * null si hubo problemas.
     *
     * @param con
     * @param dni
     * @return
     */
    public static ArrayList<String> getVehiculos(Connection con, String dni) {
        String sql = "SELECT v.mat_veh,v.marca_veh,v.kms_veh,v.precio_veh FROM vehiculos v INNER JOIN propietarios p ON(v.id_prop=p.id_prop) where p.dni_prop=?";
        ArrayList<String> result = new ArrayList<>();

        try ( PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, dni);
            String veh;
            try ( ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {
                    veh = String.join(", ", rs.getString(1),
                            rs.getString(2),
                            String.valueOf(rs.getInt(3)),
                            String.valueOf(rs.getFloat(4)));

                    result.add(veh);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PropietariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return result;
    }
    
    
    /**
     * Eliminar propietario: Recibe por parámetro el DNI de un propietario y trata de eliminarlo de la BBDD. 
     * Devuelve el número de registros eliminados.
     * @param con
     * @param dni
     * @return 
     */
    public static int delPropietario(Connection con,String dni){
        String sql = "DELETE FROM propietarios WHERE dni_prop = ?";
        int result = 0;
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, dni);
            result=pstm.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(PropietariosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
