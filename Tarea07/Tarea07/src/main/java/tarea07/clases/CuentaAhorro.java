package tarea07.clases;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Juan
 */
public class CuentaAhorro extends CuentaBancaria {

    private double tipoInteres;

    /**
     * Constructor Cuenta Ahorro
     *
     * @param tipoInteres
     * @param persona
     * @param saldo
     * @param IBAN
     */
    public CuentaAhorro(double tipoInteres, Persona persona, double saldo, String IBAN) {
        super(persona, saldo, IBAN);
        this.tipoInteres = tipoInteres;
    }

    //Getters
    public double getTipoInteres() {
        return tipoInteres;
    }

    //Setters
    public void setTipoInteres(double tipoInteres) {
        this.tipoInteres = tipoInteres;
    }

    @Override
    public String devolverInfoString() {
        return super.devolverInfoString() + " Tipo de Interes: " + tipoInteres;
    }

}
