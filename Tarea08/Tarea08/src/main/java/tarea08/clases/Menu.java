package tarea08.clases;




import java.util.Scanner;

/**
 *
 * @author Juan
 */
public class Menu {

    Scanner sc = new Scanner(System.in);
    Banco banco;

    public Menu() {
        sc = new Scanner(System.in);
        banco=new Banco();
    }

    /**
     * Inicia el programa
     */
    public void init() {
        int opcion;
        do {
            menu();
            opcion = Integer.parseInt(sc.nextLine());
            switch (opcion) {
                case 1: 
                    try {
                    newAccount();
                } catch (Exception e) {
                    System.out.println(e);
                }

                break;
                case 2:
                    listAll();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    withdraw();
                    break;
                case 5:
                    listByID();
                    break;
                case 6:
                    delete();
                    break;
                case 0:
                    System.out.println("\nSaliendo del programa...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una opción válida\n");

            }
        } while (opcion != 0);

    }

    /**
     * Muestra el menu
     */
    private void menu() {
        System.out.println("SISTEMA DE GESTION DE CUENTAS");
        System.out.println("=============================\n");
        System.out.println("-> Introduzca una opción de entre las siguientes: ");
        System.out.println("0: Salir");
        System.out.println("1: Abrir una nueva cuenta.");
        System.out.println("2: Ver un listado de las cuentas disponibles");
        System.out.println("3: Obtener los datos de una cuenta concreta.Realizar un ingreso en una cuenta.");
        System.out.println("4: Retirar efectivo de una cuenta.");
        System.out.println("5: Consultar el saldo actual de una cuenta.");
        System.out.println("6: Eliminar una cuenta(El saldo tiene que ser 0).");
        System.out.print("\nOpción: ");
    }

    private void newAccount() throws Exception {
        int opcion;
        CuentaBancaria cuenta;
        Persona p = nuevaPersona();
        System.out.println("\nCREACION DE UNA NUEVA CUENTA");
        System.out.println("------------------------------\n");

        System.out.print("Introduzca el IBAN de la cuenta corriente:");
        String iban = sc.nextLine();
        if (!iban.matches("ES[0-9]{20}")) {
            throw new Exception("IBAN incorrecto");
        }

        System.out.print("Introduzca el saldo de la cuenta corriente:");
        double saldo = Double.parseDouble(sc.nextLine());

        System.out.println("-> Introduzca una opción de entre las siguientes: ");
        System.out.println("1: Cuenta de ahorro");
        System.out.println("2: Cuenta corriente personal.");
        System.out.println("3: Cuenta corriente de empresa.");
        opcion = Integer.parseInt(sc.nextLine());
        switch (opcion) {
            case 1:
                System.out.print("Introduzca el tipo de interes:");
                double tipoInteres = Double.parseDouble(sc.nextLine());
                cuenta = new CuentaAhorro(tipoInteres, p, saldo, iban);
                banco.abrirCuenta(cuenta);
                break;
            case 2:
                System.out.print("Introduzca comisión por mantenimiento:");
                double comision = Double.parseDouble(sc.nextLine());
                cuenta = new CuentaCorrientePersonal(comision, p, saldo, iban, "");
                banco.abrirCuenta(cuenta);
                break;
            case 3:
                System.out.print("Introduzca Máximo descubierto permitido:");
                double mDescubierto = Double.parseDouble(sc.nextLine());
                System.out.print("Introduzca Tipo de interés por descubierto:");
                double tDescubierto = Double.parseDouble(sc.nextLine());
                System.out.print("Comisión fija por cada descubierto:");
                double cDescubierto = Double.parseDouble(sc.nextLine());
                cuenta = new CuentaCorrienteEmpresa(tDescubierto, mDescubierto, cDescubierto, p, saldo, iban, "");
                banco.abrirCuenta(cuenta);
                break;

            default:
                System.err.println("\nEl número introducido no se corresponde con una opción válida\n");

        }

    }

    private Persona nuevaPersona() {
        System.out.println("\nCREACION DE UNA NUEVA PERSONA");
        System.out.println("-------------------------------\n");

        System.out.print("Introduzca el nombre (sin apellidos) del empleado:");
        String nombre = sc.nextLine();

        System.out.print("Introduzca los apellidos del empleado:");
        String apellidos = sc.nextLine();

        System.out.print("Introduzca el DNI del empleado:");
        String dni = sc.nextLine();

        return new Persona(nombre, apellidos, dni);
    }

    private void listAll() {
        System.out.println("\nLISTADO DE CUENTAS");
        System.out.println("--------------------\n");
        String[] listado=banco.listadoCuentas();
        for (String list : listado) {
            System.out.println(list);
        }
    }

    private void deposit() {
        
        System.out.print("Introduzca el IBAN de la cuenta corriente:");
        String iban = sc.nextLine();
        String informacion=banco.informacionCuenta(iban);
        if(informacion!=null)
            System.out.println(informacion);
        else{
            System.out.println("IBAN incorrecto");
            return;
        }
        
        System.out.print("Introduzca la cantidad a ingresar:");
        double cantidad = Double.parseDouble(sc.nextLine());
        
        if(banco.ingresoCuenta(iban, cantidad)){
            System.out.println("Ingreso realizado");
        }else{
            System.out.println("Ingreso no realizado");
        }
    }

    private void withdraw() {
        System.out.print("Introduzca el IBAN de la cuenta corriente:");
        String iban = sc.nextLine();
        
        System.out.print("\nIntroduzca la cantidad a retirar:");
        double cantidad = Double.parseDouble(sc.nextLine());
        if(banco.retiradaCuenta(iban, cantidad)){
            System.out.println("Cantidad"+ cantidad +"€ retirada");
        }else{
            System.out.println("Cantidad no retirada");
        }
    }

    private void listByID() {
        System.out.print("Introduzca el IBAN de la cuenta corriente:");
        String iban = sc.nextLine();
        if(banco.obtenerSaldo(iban)==-1)
            System.out.println("IBAN no encontrado");
        else
            System.out.println(banco.obtenerSaldo(iban));
    }

    private void delete() {
        System.out.print("Introduzca el IBAN de la cuenta corriente:");
        String iban = sc.nextLine();
        if(banco.eliminarCuenta(iban)){
            System.out.println("Cuenta eliminada");
        }else{
            System.out.println("No se ha podido eliminar la cuenta comprueba el saldo actual.");
        }
    }
}
