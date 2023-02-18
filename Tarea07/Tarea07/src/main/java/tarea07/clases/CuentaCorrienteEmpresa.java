package tarea07.clases;

/**
 *
 * @author Juan
 */
public class CuentaCorrienteEmpresa extends CuentaCorriente {

    private double tipoInteresDescubierto;
    private double maxDescubiertoPermitido;
    private double comisionFijaDescubierto;

    /**
     *
     * @param tipoInteresDescubierto
     * @param maxDescubiertoPermitido
     * @param comisionFIjaDescubierto
     * @param persona
     * @param saldo
     * @param IBAN
     * @param listaEntidades
     */
    public CuentaCorrienteEmpresa(double tipoInteresDescubierto, double maxDescubiertoPermitido, double comisionFIjaDescubierto, Persona persona, double saldo, String IBAN, String listaEntidades) {
        super(persona, saldo, IBAN, listaEntidades);
        this.tipoInteresDescubierto = tipoInteresDescubierto;
        this.maxDescubiertoPermitido = maxDescubiertoPermitido;
        this.comisionFijaDescubierto = comisionFIjaDescubierto;
    }

    //Getters y Setters
    public double getTipoInteresDescubierto() {
        return tipoInteresDescubierto;
    }

    public void setTipoInteresDescubierto(double tipoInteresDescubierto) {
        this.tipoInteresDescubierto = tipoInteresDescubierto;
    }

    public double getMaxDescubiertoPermitido() {
        return maxDescubiertoPermitido;
    }

    public void setMaxDescubiertoPermitido(double maxDescubiertoPermitido) {
        this.maxDescubiertoPermitido = maxDescubiertoPermitido;
    }

    public double getComisionFijaDescubierto() {
        return comisionFijaDescubierto;
    }

    public void setComisionFijaDescubierto(double comisionFijaDescubierto) {
        this.comisionFijaDescubierto = comisionFijaDescubierto;
    }

    @Override
    public String devolverInfoString() {
        return super.devolverInfoString() + " Tipo Interes Descubierto: " + tipoInteresDescubierto
                + " Máximo Descubierto Permitido: " + maxDescubiertoPermitido
                + " Comisión Fija Descubierto: " + comisionFijaDescubierto;
    }
}
