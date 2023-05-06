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
public class VehiculosDAO {
    /**
     * Insertar un nuevo vehículo: Recibe por parámetro los datos del vehículo a insertar, 
     * trata de insertarlo en la BBDD y devuelve 0 si la operación se realizó con éxito o -1 en caso contrario.
     * @param con
     * @param veh
     * @return 
     */
    public static int addVehiculo(Connection con, Vehiculo veh) {
        String sql = "INSERT INTO vehiculos (mat_veh, marca_veh, kms_veh,precio_veh,desc_veh,id_prop) VALUES (?,?,?,?,?,?);";
        int result = -1;
        try ( PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, veh.getMat_veh());
            pstm.setString(2, veh.getMarca_veh());
            pstm.setInt(3, veh.getKms_veh());
            pstm.setFloat(4, veh.getPrecio_veh());
            pstm.setString(5, veh.getDesc_veh());
            pstm.setInt(6, veh.getId_prop());

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
     * Actualizar propietario de vehículo: Recibe por parámetro la matrícula de un vehículo junto al identificador de un propietario y 
     * trata de actualizar el vehículo en la BBDD. Devuelve 0 si la operación se realizó con éxito o -1 si el vehículo no existe.
     * @param con
     * @param mat
     * @param id_prop
     * @return 
     */
    public static int updatePropietario(Connection con,String mat,int id_prop){
        String sql = "UPDATE vehiculo SET mat_veh = ? WHERE id_prop = ?";
        int result=-1;
        try ( PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, mat);
            pstm.setInt(2, id_prop);
            result=pstm.executeUpdate();
            if (result > 0) {
                return 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VehiculosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }
    
    /**
     * Eliminar vehículo: Recibe por parámetro la matrícula de un vehículo y trata de eliminarlo de la BBDD. 
     * Devuelve 0 si la operación se realizó con éxito o -1 si el vehículo no existe.
     * @param con
     * @param mat
     * @return 
     */
    public static int deleteVehiculo(Connection con,String mat){
        String sql = "DELETE FROM vehiculos WHERE mat_veh = ?";
        int result = -1;
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, mat);
            result=pstm.executeUpdate();
            if (result > 0) {
                return 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PropietariosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    /**
     * Recuperar todos los vehículos: No recibe parámetros y devuelve una lista con todos los vehículos del concesionario.
     * Para cada vehículo, la lista contendrá una cadena de caracteres con los datos del mismo, incluido el nombre del propietario
     * @param con
     * @return 
     */
    public static ArrayList<String> getVehiculos(Connection con){
        String sql = "SELECT v.mat_veh, v.marca_veh,v.kms_veh,v.precio_veh,p.nombre_prop FROM vehiculos v INNER JOIN propietarios p ON(v.id_prop=p.id_prop)";
        ArrayList<String> result = new ArrayList<>();

        try ( PreparedStatement pstm = con.prepareStatement(sql)) {

            String veh;
            try ( ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {
                    veh = String.join(", ", rs.getString(1),
                            rs.getString(2),
                            String.valueOf(rs.getInt(3)),
                            String.valueOf(rs.getFloat(4)),
                            rs.getString(5));

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
     * Recuperar vehículos por marca: Recibe una marca por parámetro y devuelve una lista con el listado de vehículos de la citada marca. 
     * Para cada vehículo, la lista contendrá una cadena de caracteres con los datos del mismo, incluido el nombre del propietario. 
     * Si no existen vehículos, devuelve el ArrayList vacío.
     * 
     * @param con
     * @param marca
     * @return 
     */
    public static ArrayList<String> getVehiculos(Connection con,String marca){
        String sql = "SELECT v.mat_veh, v.marca_veh,v.kms_veh,v.precio_veh,p.nombre_prop FROM vehiculos v INNER JOIN propietarios p ON(v.id_prop=p.id_prop) where v.marca_veh=?";
        ArrayList<String> result = new ArrayList<>();

        try ( PreparedStatement pstm = con.prepareStatement(sql)) {

            String veh;
            pstm.setString(1, marca);
            try ( ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {
                    veh = String.join(", ", rs.getString(1),
                            rs.getString(2),
                            String.valueOf(rs.getInt(3)),
                            String.valueOf(rs.getFloat(4)),
                            rs.getString(5));

                    result.add(veh);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(PropietariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return result;
    }
//Recuperar vehículos: No recibe parámetros (solo la coneción con la BBDD) y retorna una lista con la matrícula, marca, kilómetros y precio de cada vehículo.
    public static ArrayList<String> getVehiculosCorta(Connection con){
        String sql = "SELECT mat_veh, marca_veh,kms_veh,precio_veh FROM vehiculos";
        ArrayList<String> result = new ArrayList<>();

        try ( PreparedStatement pstm = con.prepareStatement(sql)) {

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
}
