/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prog07_tarea.clases;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author
 */
public class Banco {

    final int MAX_CUENTAS = 100;
    ArrayList<CuentaBancaria> listaCuentas;
    int numCuentas;

    public Banco() {

        listaCuentas = new ArrayList();
    }

    //---------------------------------------------------------------        
    // MÉTODO abrirCuenta: Creación de una nueva cuenta bancaria
    //--------------------------------------------------------------- 
    public boolean abrirCuenta(CuentaBancaria cuenta) throws Exception {

        if (buscaCuenta(cuenta.obtenerCCC()) != null) {
            return false;
        }

        // Ingresamos el saldo inicial en la cuenta
        if (numCuentas < MAX_CUENTAS) {
            listaCuentas.add(cuenta);
            return true;
        }

        return false;
    }

    public CuentaBancaria buscaCuenta(String ccc) {

        Iterator<CuentaBancaria> it = listaCuentas.iterator();

        while (it.hasNext()) {
            if (it.next().obtenerCCC().equals(ccc)) {
                return it.next();
            }
        }
        return null;
    }

    //---------------------------------------------------------------        
    // MÉTODO listadoCuentas: Devuelve un array de cadenas con la información de cada cuenta.
    //---------------------------------------------------------------       
    public String[] listadoCuentas() throws Exception {
        String cuentas[] = new String[numCuentas];
        System.out.println("LISTADO DE CUENTAS DISPONIBLES");

        listaCuentas.forEach(cuenta -> {
            System.out.println(cuenta.devolverContenidoString());
        });
        return cuentas;
    }

    //---------------------------------------------------------------        
    // MÉTODO informacionCuenta: Devuelve String con información de una cuenta
    //---------------------------------------------------------------      
    public String informacionCuenta(String iban) {
        CuentaBancaria cuenta = buscaCuenta(CuentaBancaria.obtenerNumeroCuenta(iban));
        if (cuenta != null) {
            return cuenta.devolverContenidoString();
        }
        return null;
    }

    //---------------------------------------------------------------        
    // MÉTODO ingresoCuenta: Ingreso de una cantidad en una cuenta
    //---------------------------------------------------------------       
    public boolean ingresoCuenta(String iban, double cantidad) throws Exception {

        CuentaBancaria cuenta = buscaCuenta(CuentaBancaria.obtenerNumeroCuenta(iban));
        if (cuenta != null) {
            cuenta.ingresar(cantidad);
            return true;
        }
        return false;
    }

    public boolean retiradaCuenta(String iban, double cantidad) throws Exception {

        CuentaBancaria cuenta = buscaCuenta(CuentaBancaria.obtenerNumeroCuenta(iban));
        if (cuenta != null) {
            cuenta.retirar(cantidad);
            return true;
        }
        return false;
    }

    public double obtenerSaldo(String iban) throws Exception {

        CuentaBancaria cuenta = buscaCuenta(CuentaBancaria.obtenerNumeroCuenta(iban));
        if (cuenta != null) {
            return cuenta.obtenerSaldo();
        }
        return -1;
    }
    
    public boolean eliminarCuenta (String iban) throws Exception {

        Iterator<CuentaBancaria> it = listaCuentas.iterator();

        while (it.hasNext()) {
            CuentaBancaria cuenta=it.next();
            if (cuenta.obtenerCCC().equals(CuentaBancaria.obtenerNumeroCuenta(iban))) {
                if (cuenta.obtenerSaldo()==0){
                    it.remove();
                return true;
                }
            }
        }
        return false;
    }
}
