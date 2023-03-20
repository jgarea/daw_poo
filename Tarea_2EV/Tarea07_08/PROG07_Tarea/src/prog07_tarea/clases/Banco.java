/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prog07_tarea.clases;


/**
 *
 * @author
 */
public class Banco {

    final int MAX_CUENTAS=100;
    CuentaBancaria listaCuentas[];
    int numCuentas;

    public Banco() {
        
        listaCuentas = new CuentaBancaria[MAX_CUENTAS];
        numCuentas = 0;
    }

    //---------------------------------------------------------------        
    // MÉTODO abrirCuenta: Creación de una nueva cuenta bancaria
    //--------------------------------------------------------------- 
    public boolean abrirCuenta(CuentaBancaria cuenta) throws Exception {
        
        if (buscaCuenta(cuenta.obtenerCCC())!=null)
            return false;
        
        // Ingresamos el saldo inicial en la cuenta
        if (numCuentas <  MAX_CUENTAS) {
            listaCuentas[numCuentas] = cuenta;
            numCuentas++;
            return true;
        }
        
        return false;
    }

    public CuentaBancaria buscaCuenta(String ccc) {
        for (int i = 0; i < numCuentas; i++) {
            if (listaCuentas[i].obtenerCCC().equals(ccc)) {
                return listaCuentas[i];
            }
        }
        return null;
    }
    
    //---------------------------------------------------------------        
    // MÉTODO listadoCuentas: Devuelve un array de cadenas con la información de cada cuenta.
    //---------------------------------------------------------------       
    public String[] listadoCuentas () throws Exception {
        String cuentas[]=new String[numCuentas];
        System.out.println("LISTADO DE CUENTAS DISPONIBLES");
        for (int i=0; i<numCuentas;i++){
            cuentas[i]=listaCuentas[i].devolverContenidoString();
        }
        return cuentas;
    }  
    
    //---------------------------------------------------------------        
    // MÉTODO informacionCuenta: Devuelve String con información de una cuenta
    //---------------------------------------------------------------      
    public String informacionCuenta (String iban){
        CuentaBancaria cuenta=buscaCuenta(CuentaBancaria.obtenerNumeroCuenta(iban));
        if (cuenta!=null){
            return cuenta.devolverContenidoString();
        }
        return null;
    }
    
     //---------------------------------------------------------------        
    // MÉTODO ingresoCuenta: Ingreso de una cantidad en una cuenta
    //---------------------------------------------------------------       
    public boolean ingresoCuenta(String iban, double cantidad) throws Exception {

        CuentaBancaria cuenta=buscaCuenta(CuentaBancaria.obtenerNumeroCuenta(iban));
        if (cuenta!=null){
            cuenta.ingresar(cantidad);
            return true;
        }
        return false;
    }
    
   public boolean retiradaCuenta(String iban, double cantidad) throws Exception {

        CuentaBancaria cuenta=buscaCuenta(CuentaBancaria.obtenerNumeroCuenta(iban));
        if (cuenta!=null){
            cuenta.retirar(cantidad);
            return true;
        }
        return false;
    }
  
    public double obtenerSaldo(String iban) throws Exception {

        CuentaBancaria cuenta=buscaCuenta(CuentaBancaria.obtenerNumeroCuenta(iban));
        if (cuenta!=null){
            return cuenta.obtenerSaldo();
        }
        return -1;
    }
}
