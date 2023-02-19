package tarea08.clases;



import tarea08.interfaces.Imprimible;

/**
 *
 * @author Juan
 */
public abstract class CuentaBancaria implements Imprimible{
    private Persona persona;
    private double saldo;
    private String IBAN;

    /**
     * Constructor
     * @param persona
     * @param saldo
     * @param IBAN 
     */
    public CuentaBancaria(Persona persona, double saldo, String IBAN) {
        this.persona = persona;
        this.saldo = saldo;
        this.IBAN = IBAN;
    }

    //Getters y setters
    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    @Override
    public String devolverInfoString() {
        return persona.devolverInfoString() + " Cuenta: " + IBAN + " Saldo: " + saldo;   
    }

    
}
