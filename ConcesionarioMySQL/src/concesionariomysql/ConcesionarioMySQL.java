package concesionariomysql;

import RDB.DB;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xavi
 */
public class ConcesionarioMySQL {
    public static void main(String[] args) {
        DB.URL="jdbc:mariadb://192.168.122.2:3306/Concesionario?allowPublicKeyRetrieval=true&useSSL=false";
        DB.USER="concesionario";
        DB.PASS="concesionario";
        try (DB db=DB.open()) {
            menuPrincipal();
        } catch (SQLException ex) {
            System.out.println("Erro na base de datos: "+ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro cargando driver da base de datos: "+ex.getMessage());
        }
    }

    public static void menuPrincipal() {
        Scanner sc = new Scanner(System.in);
        int opcion=0;
        do {
            System.out.println("********** MENU **********");
            System.out.println("1. Xestión de vehículos");
            System.out.println("2. Xestión de propietarios");
            System.out.println("3. Saír");
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
                }
            } catch(NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        } while (opcion != 3);
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
                        if (v!=null) VehiculosDAO.insertarVehiculo(v);
                        break;
                    case 2:
                        matricula=XestionVehiculos.getMatricula();
                        if (matricula!=null) {
                            v=VehiculosDAO.recuperarVehiculo(matricula);
                            if (v==null) System.out.println("Non existe o vehículo "+matricula);
                            else {
                                System.out.println("Se borrará: "+v);
                                VehiculosDAO.eliminarVehiculo(v);
                                System.out.println("Vehículo borrado");
                            }
                        }
                        break;
                    case 3:
                        matricula=XestionVehiculos.getMatricula();
                        if (matricula!=null) {
                            v=VehiculosDAO.recuperarVehiculo(matricula);
                            if (v==null) System.out.println("Non existe o vehículo "+matricula);
                            else {
                                System.out.println("Modificando: "+v);
                                v=XestionVehiculos.formVehiculo(v);
                                if (v!=null) {
                                    System.out.println("Novos datos: "+v);
                                    VehiculosDAO.actualizarVehiculo(v);
                                    System.out.println("Vehículo modificado");
                                }
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Venta de Vehiculo");
                        dni=XestionPropietarios.getDni();
                        if (dni!=null) {
                            Propietario p=PropietariosDAO.recuperarPropietario(dni);
                            if (p==null) System.out.println("O Cliente non existe");
                            else {
                                do {
                                    System.out.println("Comprador: "+p);
                                    matricula=XestionVehiculos.getMatricula();
                                    if (matricula==null) break;
                                    v=VehiculosDAO.recuperarVehiculo(matricula);
                                    if (v==null) System.out.println("O Vehiculo non existe");
                                    else {
                                        if (v.getPropietario()!=null) System.out.println("O Vehiculo xa ten dono");
                                        else {
                                            System.out.println("Compra de: "+v);
                                            VehiculosDAO.actualizarPropietarioVehiculo(v,p);
                                            System.out.println("Venta rexistrada");
                                        }
                                    }
                                } while(v==null);
                            }
                        }
                        break;
                    case 5:
                        System.out.println("Listado de Vehículos");
                        ArrayList<Vehiculo> lista=VehiculosDAO.recuperarVehiculos();
                        for(Vehiculo lv: lista) System.out.println(lv);
                }
            } catch (SQLException ex) {
                System.out.println("ERROR: "+ex.getMessage());
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
                        if (p!=null) PropietariosDAO.insertarPropietario(p);
                        break;
                    case 2:
                        dni=XestionPropietarios.getDni();
                        if (dni!=null) {
                            p=PropietariosDAO.recuperarPropietario(dni);
                            if (p==null) System.out.println("Non existe o propietario "+dni);
                            else {
                                System.out.println(p);
                                Vehiculo[] v=p.getVehiculos();
                                if (v!=null) {
                                    System.out.println("Propietario dos seguintes vehículos:");
                                    for(Vehiculo ve:v) {
                                        System.out.println("\t"+ve);
                                    }
                                } else {
                                    System.out.print("(B)orrar ou voltar ao menú ?");
                                    if (sc.nextLine().equals("B")) {
                                        PropietariosDAO.eliminaPropietario(p);
                                        System.out.println("Propietario borrado");
                                    }
                                }
                            }
                        }
                        break;
                    case 3:
                        dni=XestionPropietarios.getDni();
                        if (dni!=null) {
                            p=PropietariosDAO.recuperarPropietario(dni);
                            if (p==null) System.out.println("Non existe o propietario "+dni);
                            else {
                                System.out.println("Modificando: "+p);
                                p=XestionPropietarios.formPropietario(p);
                                PropietariosDAO.updatePropietario(p);
                                System.out.println("Propietario Modificado");
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Listado de Clientes");
                        ArrayList<Propietario> lista=PropietariosDAO.cargarPropietarios();
                        for(Propietario lp: lista) System.out.println(lp);
                        break;
                }
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        } while (opcion != 5);
            
    }
}
