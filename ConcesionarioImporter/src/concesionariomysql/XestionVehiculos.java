package concesionariomysql;

import java.util.Scanner;

/**
 *
 * @author xavi
 */
public class XestionVehiculos {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static Vehiculo formVehiculo(Vehiculo v) {
        String matricula=null,modelo=null,marca=null;
        Propietario propietario=null;
        String input;
        String dmod="",dmar="";
        if (v!=null) {
            matricula=v.getMatricula();
            modelo=v.getModelo();
            marca=v.getMarca();
            propietario=v.getPropietario();
            dmod=" ["+modelo+"] ";
            dmar=" ["+marca+"] ";
        }

        if (matricula==null) {
            matricula=getMatricula();
        } else System.out.println("Modificando vehículo "+matricula);
        
        System.out.println("Introduce el modelo del vehículo (* para cancelar) :"+dmod);
        input = scanner.nextLine();
        if (input.startsWith("*")) return null;
        if (input.length() > 0) modelo=input; 


        System.out.println("Introduce la marca del vehículo (* para cancelar):"+dmar);
        input = scanner.nextLine();
        if (input.startsWith("*")) return null;
        if (input.length() > 0) marca=input; 
        if (matricula==null || marca==null || modelo==null) return null;

        v=new Vehiculo(matricula, marca, modelo);
        if (propietario!=null) v.setPropietario(propietario);
        return v;
    }
    
    public static String getMatricula() {
        System.out.println("Introduce la matrícula del vehículo (* para cancelar) :");
        String matricula = scanner.nextLine();
        if (matricula.startsWith("*")) return null;
        return matricula;
    }
}
