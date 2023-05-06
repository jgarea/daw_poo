package com.prog11.princ;

import com.prog11.bbdd.*;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class Prog11_Principal {

    public static void main(String[] args) {
        ConnectionDB db= new ConnectionDB();
        Connection con= db.openConnection();
        
        //Inserción de dos propietarios
        System.out.println("Insertando dos propietarios ...");
        Propietario p1 = new Propietario(1, "Pepe Pérez", "08876543S");
        Propietario p2 = new Propietario(2, "María Gutiérrez", "33333444R");
        if (PropietariosDAO.addPropietario(con, p1) == -1) {
            System.out.println("Error al insertar Propietario");
        }

        if (PropietariosDAO.addPropietario(con, p2) == -1) {
            System.out.println("Error al insertar Propietario");
        }

        Vehiculo v1 = new Vehiculo("1111DBJ", "Fiat", 100000, 10000, "Fiat Stilo 115D", 1);
        Vehiculo v2 = new Vehiculo("3333KKJ", "Seat", 130000, 15000, "Seat León FR", 1);
        Vehiculo v3 = new Vehiculo("9988DDD", "Renault", 120000, 11000, "Renault Clio", 2);
        System.out.println("Insertando varios vehículos ...");
        if (VehiculosDAO.addVehiculo(con, v1) == -1) {
            System.out.println("Error al insertar Vehículo");
        }

        if (VehiculosDAO.addVehiculo(con, v2) == -1) {
            System.out.println("Error al insertar Vehículo");
        }

        if (VehiculosDAO.addVehiculo(con, v3) == -1) {
            System.out.println("Error al insertar Vehículo");
        }
        

        System.out.println("Listando todos los vehículos ...");

        ArrayList<String> lista =VehiculosDAO.getVehiculos(con);
        for (String reg : lista) {
            System.out.println(reg);
        }

        System.out.println("Eliminado un vehículo que no existe");

        if (VehiculosDAO.deleteVehiculo(con, "7788LLL") == -1) {
            System.out.println("El vehículo no existe");
        }

        System.out.println("Eliminado un vehículo que existe");

        if (VehiculosDAO.deleteVehiculo(con, "9988DDD") == -1) {
            System.out.println("El vehículo no existe");
        }

        System.out.println("Listando todos los vehículos ...");
        lista = (VehiculosDAO.getVehiculos(con));
        for (String reg : lista) {
            System.out.println(reg);
        }
        System.out.println("Mostrando vehículos de una marca");
        lista = (VehiculosDAO.getVehiculos(con, "Seat"));
        for (String reg : lista) {
            System.out.println(reg);
        }

        System.out.println("Listando todos los vehículos de un propietario...");
        //Obtenemos una lista de vehículos de un propietarios.
        lista = (PropietariosDAO.getVehiculos(con, "08876543S"));
        for (String reg : lista) {
            System.out.println(reg);
        }

        //Eliminamos los propietarios.
        PropietariosDAO.delPropietario(con, "08876543S");
        PropietariosDAO.delPropietario(con, "33333444R");
        
        //Cerramos conexion
        db.closeConnection();
    }
}
