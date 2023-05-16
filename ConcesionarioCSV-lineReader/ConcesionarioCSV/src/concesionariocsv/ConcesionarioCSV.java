package concesionariocsv;

import database.DatabaseException;
import java.io.IOException;
import java.util.Scanner;

public class ConcesionarioCSV {
    private static VehiculoDatabase dbv;
    private static PropietarioDatabase dbp;

    public static void main(String[] args) {
        String ppath=ConcesionarioCSV.class.getResource("/resources/Propietarios.csv").getPath();        
        String vpath=ConcesionarioCSV.class.getResource("/resources/Vehiculos.csv").getPath();        
        try {
            // E importante que esté primeiro, porque precisamos ter os propietarios
            // para cargar os vehiculos
            dbp=new PropietarioDatabase(ppath); 
            dbv=new VehiculoDatabase(vpath);
            menuPrincipal();
        } catch (IOException ex) {
            System.out.println("ERROR: "+ex.getMessage());
        } 
    }

    public static void menuPrincipal() {
        Scanner sc = new Scanner(System.in);
        int opcion=0;
        do {
            System.out.println("********** MENU **********");
            System.out.println("1. Xestión de vehículos");
            System.out.println("2. Xestión de propietarios");
            System.out.println("3. Mantemento");
            System.out.println("4. Saír");
            System.out.println("****************************");
            System.out.print("Escolla unha opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1:
                        menuVehiculos();
                        break;
                    case 2:
                        menuPropietarios();
                        break;
                    case 3:
                        Vehiculo[] lista=dbv.select((c)->true);
                        dbp.pack();
                        dbv.clear();
                        for(Vehiculo v:lista) dbv.insert(v);
                        break;
                }
            } catch(IOException | NumberFormatException | DatabaseException e) {
                System.out.println(e.getMessage());
            }
        } while (opcion != 4);
    }
    
    public static void menuVehiculos() {
        Scanner sc = new Scanner(System.in);
        int opcion=0;
        do {
            Vehiculo v=null;
            String matricula;
            String dni;
            
            System.out.println("********** XESTIÓN DE VEHÍCULOS **********");
            System.out.println("1. Dar de alta un vehículo");
            System.out.println("2. Dar de baixa un vehículo");
            System.out.println("3. Modificar un vehículo");
            System.out.println("4. Venta de Vehículo");
            System.out.println("5. Listado de Vehículos");
            System.out.println("6. Volver ao menú principal");
            System.out.println("******************************************");
            System.out.print("Escolla unha opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1:
                        v=XestionVehiculos.formVehiculo(null);
                        if (v!=null) dbv.insert(v);
                        break;
                    case 2:
                        matricula=XestionVehiculos.getMatricula();
                        if (matricula!=null) {
                            v=dbv.get(matricula);
                            if (v==null) System.out.println("Non existe o vehículo "+matricula);
                            else {
                                System.out.println("Se borrará: "+v);
                                dbv.delete(v);
                                System.out.println("Vehículo borrado");
                            }
                        }
                        break;
                    case 3:
                        matricula=XestionVehiculos.getMatricula();
                        if (matricula!=null) {
                            v=dbv.get(matricula);
                            if (v==null) System.out.println("Non existe o vehículo "+matricula);
                            else {
                                System.out.println("Modificando: "+v);
                                v=XestionVehiculos.formVehiculo(v);
                                if (v!=null) {
                                    System.out.println("Novos datos: "+v);
                                    dbv.update(v);
                                    System.out.println("Vehículo modificado");
                                }
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Venta de Vehiculo");
                        dni=XestionPropietarios.getDni();
                        if (dni!=null) {
                            Propietario p=dbp.get(dni);
                            if (p==null) System.out.println("O Cliente non existe");
                            else {
                                do {
                                    System.out.println("Comprador: "+p);
                                    matricula=XestionVehiculos.getMatricula();
                                    if (matricula==null) break;
                                    v=dbv.get(matricula);
                                    if (v==null) System.out.println("O Vehiculo non existe");
                                    else {
                                        if (v.getPropietario()!=null) System.out.println("O Vehiculo xa ten dono");
                                        else {
                                            System.out.println("Compra de: "+v);
                                            v.setPropietario(p);
                                            dbv.update(v);
                                            System.out.println("Venta rexistrada");
                                        }
                                    }
                                } while(v==null);
                            }
                        }
                        break;
                    case 5:
                        System.out.println("Listado de Vehículos");
                        Vehiculo[] lista=dbv.select((c)->true);
                        for(Vehiculo lv: lista) System.out.println(lv);
                }
            } catch(DatabaseException | NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        } while (opcion != 6);
    }
    
    public static void menuPropietarios() {
        Scanner sc = new Scanner(System.in);
        int opcion=0;
        do {
            Propietario p;
            String dni;
            
            System.out.println("********** XESTIÓN DE PROPIETARIOS **********");
            System.out.println("1. Dar de alta un propietario");
            System.out.println("2. Consultar/Dar de baixa un propietario");
            System.out.println("3. Modificar un propietario");
            System.out.println("4. Listado de Clientes");
            System.out.println("5. Volver ao menú principal");
            System.out.println("*********************************************");
            System.out.print("Escolla unha opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1:
                        p=XestionPropietarios.formPropietario(null);
                        if (p!=null) dbp.insert(p);
                        break;
                    case 2:
                        dni=XestionPropietarios.getDni();
                        if (dni!=null) {
                            p=dbp.get(dni);
                            if (p==null) System.out.println("Non existe o propietario "+dni);
                            else {
                                System.out.println(p);
                                Vehiculo[] v=dbv.select((ve)->ve.getPropietario().getDni().equals(dni));
                                if (v!=null) {
                                    System.out.println("Propietario dos seguintes vehículos:");
                                    for(Vehiculo ve:v) {
                                        System.out.println("\t"+ve);
                                    }
                                } else {
                                    System.out.print("(B)orrar ou voltar ao menú ?");
                                    if (sc.nextLine().equals("B")) {
                                        dbp.delete(p);
                                        System.out.println("Propietario borrado");
                                    }
                                }
                            }
                        }
                        break;
                    case 3:
                        dni=XestionPropietarios.getDni();
                        if (dni!=null) {
                            p=dbp.get(dni);
                            if (p==null) System.out.println("Non existe o propietario "+dni);
                            else {
                                System.out.println("Modificando: "+p);
                                p=XestionPropietarios.formPropietario(p);
                                dbp.update(p);
                                System.out.println("Propietario Modificado");
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Listado de Clientes");
                        Propietario[] lista=dbp.select((c)->true);
                        for(Propietario lp: lista) System.out.println(lp);
                        break;
                }
            } catch(DatabaseException | NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        } while (opcion != 5);
            
    }
    
    /**
     * Retorna o Propietario almacenado na liña nline do CSV, sendo o primeiro
     * propietario a liña 1 e contando as liñas dos propietarios borrados
     */
    public static Propietario getPropietario(int nline) throws DatabaseException {
        return dbp.getPropietario(nline);
    }
   
    /**
     * Dado un propietario retorna a liña na que se atopa no ficheiro CSV ou 
     * -1 si non está.
     */
    public static int getLine(Propietario p) throws DatabaseException {
        return dbp.getLine(p.getDni());
    }
}
