package tarea07.clases;

/**
 *
 * @author Juan
 */
public class Banco {

    private final int MAX_CUENTAS = 100;
    private CuentaBancaria[] cuentas;
    private int cont;

    public Banco() {
        cuentas = new CuentaBancaria[MAX_CUENTAS];
        cont = 0;
    }

    /**
     * Recibe por parámetro un objeto CuentaBancaria y lo almacena en la
     * estructura. Devuelve true o false indicando si la operación se realizó
     * con éxito.
     *
     * @param cuenta
     * @return
     */
    public boolean abrirCuenta(CuentaBancaria cuenta) {
        if (cont < MAX_CUENTAS) {
            cuentas[cont] = cuenta;
            cont++;
            return true;
        }
        return false;
    }

    /**
     * No recibe parámetro y devuelve un array donde cada elemento es una cadena
     * que representa la información de una cuenta.
     *
     * @return
     */
    public String[] listadoCuentas() {
        String[] listado = new String[cont];
        for (int i = 0; i < cont; i++) {
            listado[i] = cuentas[i].devolverInfoString();
        }
        return listado;
    }

    /**
     * Recibe un iban por parámetro y devuelve una cadena con la información de
     * la cuenta o null si la cuenta no existe.
     *
     * @param iban
     * @return
     */
    public String informacionCuenta(String iban) {
        for (int i = 0; i < cont; i++) {
            if (cuentas[i].getIBAN().equals(iban)) {
                return cuentas[i].devolverInfoString();
            }
        }
        return null;
    }

    /**
     * Recibe un iban por parámetro y una cantidad e ingresa la cantidad en la
     * cuenta. Devuelve true o false indicando si la operación se realizó con
     * éxito.
     *
     * @param iban
     * @param cantidad
     * @return
     */
    public boolean ingresoCuenta(String iban, double cantidad) {
        for (int i = 0; i < cont; i++) {
            if (cuentas[i].getIBAN().equals(iban)) {
                cuentas[i].setSaldo(cuentas[i].getSaldo() + cantidad);
                return true;
            }
        }
        return false;
    }

    /**
     * Recibe un iban por parámetro y una cantidad y trata de retirar la
     * cantidad de la cuenta. Devuelve true o false indicando si la operación se
     * realizó con éxito.
     *
     * @param iban
     * @param cantidad
     * @return
     */
    public boolean retiradaCuenta(String iban, double cantidad) {
        for (int i = 0; i < cont; i++) {
            if (cuentas[i].getIBAN().equals(iban)) {
                if ((cuentas[i].getSaldo() - cantidad) > 0) {
                    cuentas[i].setSaldo(cuentas[i].getSaldo() - cantidad);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recibe un iban por parámetro y devuelve el saldo de la cuenta si existe.
     * En caso contrario devuelve -1.
     *
     * @param iban
     * @return
     */
    public double obtenerSaldo(String iban) {
        for (int i = 0; i < cont; i++) {
            if (cuentas[i].getIBAN().equals(iban)) {

                return cuentas[i].getSaldo();
            }
        }

        return -1;
    }

}
