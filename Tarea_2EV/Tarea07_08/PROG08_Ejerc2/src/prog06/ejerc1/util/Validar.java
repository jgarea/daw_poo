/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prog06.ejerc1.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 *
 * @author 
 */
public class Validar {
    
    public static boolean comparaFecha (LocalDate fecha_mat){
        return fecha_mat.isBefore(LocalDate.now());
    }
    
    public static boolean validaDNI (String dni){
        return dni.matches("^[0-9]{8}[A-Z]$");  //Versión simple para validar un DNI.
        
    }
    
    public static boolean validaMatricula (String matricula){        
        return matricula.matches("^[0-9]{4}[A-Z]{3}$"); //Versión simple para validar una matrícula.
    }
    
    public static boolean validaNombre (String nombre){
        
        if (nombre.length()>40) return false; //comprobamos que el tamaño no supera los 40 caracteres
         
        int posicion = nombre.indexOf(" "); //buscamos el primer espacio en blanco.
        
        if (posicion==-1) return false; //Si no existe
        else {
            posicion=nombre.indexOf(" ", posicion+1); //Buscamos el siguiente espacio en blanco.
            if (posicion==-1) return false; //Si no lo encontramos
            
        }
        return true;
        
    }
    
}
