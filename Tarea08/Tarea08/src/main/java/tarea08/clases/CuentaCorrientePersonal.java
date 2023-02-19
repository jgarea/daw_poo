package tarea08.clases;




/**
 *
 * @author Juan
 */
public class CuentaCorrientePersonal extends CuentaCorriente{
    
    private double comisionMantenimiento;

    /**
     * Constructor
     * @param comisionMantenimiento
     * @param persona
     * @param saldo
     * @param IBAN
     * @param listaEntidades 
     */
    public CuentaCorrientePersonal(double comisionMantenimiento, Persona persona, double saldo, String IBAN, String listaEntidades) {
        super(persona, saldo, IBAN, listaEntidades);
        this.comisionMantenimiento = comisionMantenimiento;
    }

    //Getters y setters
    public double getComisionMantenimiento() {
        return comisionMantenimiento;
    }

    public void setComisionMantenimiento(double comisionMantenimiento) {
        this.comisionMantenimiento = comisionMantenimiento;
    }
    @Override
    public String devolverInfoString() {
        return super.devolverInfoString() + " Comisión de mantenimiento: " + comisionMantenimiento;
    }

}
