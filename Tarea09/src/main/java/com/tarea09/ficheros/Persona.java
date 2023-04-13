package com.tarea09.ficheros;



import com.tarea09.interfaces.Imprimible;

import java.io.Serializable;

/**
 *
 * @author Juan
 */
public class Persona implements Imprimible, Serializable {

    private String nombre;
    private String apellidos;
    private String dni;

    /**
     * Constructor persona
     *
     * @param nombre
     * @param apellidos
     * @param dni
     */
    public Persona(String nombre, String apellidos, String dni) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
    }

    /**
     * Obtener el nombre
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modificar nombre
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtener Apellidos
     *
     * @return
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Modificar Apellidos
     *
     * @param apellidos
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtener DNI
     *
     * @return
     */
    public String getDni() {
        return dni;
    }

    /**
     * Modificar DNI
     *
     * @param dni
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Devuelve la información de la persona
     *
     * @return
     */
    @Override
    public String devolverInfoString() {
        return "Nombre: " + nombre + " Apellidos: " + apellidos + " DNI: " + dni;
    }

}
