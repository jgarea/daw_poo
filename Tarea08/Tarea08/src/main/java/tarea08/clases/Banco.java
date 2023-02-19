package tarea08.clases;

import java.util.ArrayList;
import java.util.Iterator;



/**
 *
 * @author Juan
 */
public class Banco {

    
    //Usamos una estructura de datos dinámica en este caso un ArrayList para almacenar las cuentas bancarias.
    private ArrayList<CuentaBancaria> cuentas;
    final int MAX_CUENTAS = 100;
    private int cont;

    public Banco() {
        cuentas = new ArrayList<>();
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
            cuentas.add(cuenta);
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
        Iterator<CuentaBancaria> it=cuentas.iterator();
        int i=0;
        while(it.hasNext()){
            listado[i]=it.next().devolverInfoString();
            i++;
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
        CuentaBancaria c=buscaCuenta(iban);
        if(c!=null){
            return c.devolverInfoString();
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
        CuentaBancaria c=buscaCuenta(iban);
        if(c==null)
            return false;
        
        c.setSaldo(c.getSaldo()+cantidad);
        
        return true;
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
        CuentaBancaria c=buscaCuenta(iban);
        if(c==null)
            return false;
        
        if((c.getSaldo()-cantidad)<0)
            return false;
        
        c.setSaldo(c.getSaldo()-cantidad);
        
        return true;
    }

    /**
     * Recibe un iban por parámetro y devuelve el saldo de la cuenta si existe.
     * En caso contrario devuelve -1.
     *
     * @param iban
     * @return
     */
    public double obtenerSaldo(String iban) {
        CuentaBancaria c=buscaCuenta(iban);
        if(c==null)
            return -1;

        return c.getSaldo();
    }
    
    /**
     * Función para buscar cuenta
     * @param iban
     * @return 
     */
    public CuentaBancaria buscaCuenta(String iban) {

        Iterator<CuentaBancaria> it = cuentas.iterator();
        CuentaBancaria c;
        while (it.hasNext()) {
            c=it.next();
            if (c.getIBAN().equals(iban)) {
                return c;
            }
        }
        return null;
    }

}
