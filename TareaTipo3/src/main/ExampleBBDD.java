/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tipo3.DB;
import tipo3.Storage;
import tipo3.UsuariosStorageDDBB;

/**
 *
 * @author xavi
 */
public class ExampleBBDD {
    public static void main(String[] args) {
        DB.URL="jdbc:mariadb://192.168.122.2:3306/mi_bd?allowPublicKeyRetrieval=true&useSSL=false";
        DB.USER="user";
        DB.PASS="pass";
        try(DB db=DB.open();
            Storage storage=new UsuariosStorageDDBB();
            ) {    
            Example example=new Example(storage);
            example.aplicacion();
        } catch (Exception ex) {
           System.out.println("Error: "+ex.getMessage());
        }
    }
}
