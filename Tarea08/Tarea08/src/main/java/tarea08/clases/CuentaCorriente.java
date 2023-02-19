package tarea08.clases;



/**
 *
 * @author Juan
 */
public abstract class CuentaCorriente extends CuentaBancaria {

    private String listaEntidades;

    /**
     * Constructor
     *
     * @param persona
     * @param saldo
     * @param IBAN
     * @param listaEntidades
     */
    public CuentaCorriente(Persona persona, double saldo, String IBAN, String listaEntidades) {
        super(persona, saldo, IBAN);
        this.listaEntidades = listaEntidades;
    }

    //Getters y setters
    public String getListaEntidades() {
        return listaEntidades;
    }

    public void setListaEntidades(String listaEntidades) {
        this.listaEntidades = listaEntidades;
    }

    @Override
    public String devolverInfoString() {
        return super.devolverInfoString() + " Lista de entidades: " + listaEntidades;
    }

}
