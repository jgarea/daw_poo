package concesionariomysql;

import java.util.Scanner;

/**
 *
 * @author xavi
 */
class XestionPropietarios {
    private static final Scanner scanner = new Scanner(System.in);

     public static Propietario formPropietario(Propietario p) {
        String dni=null,nome=null,apelidos=null;
        String input;
        String dnom="",dape="";
        if (p!=null) {
            dni=p.getDni();
            nome=p.getNome();
            apelidos=p.getApelidos();
            dnom=" ["+nome+"] ";
            dape=" ["+apelidos+"] ";
        }

        if (dni==null) {
            dni=getDni();
        } else System.out.println("Modificando propietario "+dni);
        
        System.out.println("Nome (* para cancelar) :"+dnom);
        input = scanner.nextLine();
        if (input.startsWith("*")) return null;
        if (input.length() > 0) nome=input; 


        System.out.println("Apelidos (* para cancelar):"+dape);
        input = scanner.nextLine();
        if (input.startsWith("*")) return null;
        if (input.length() > 0) apelidos=input; 
        if (dni==null || nome==null || apelidos==null) return null;

        return new Propietario(dni,nome,apelidos);
    }
    
    
    static String getDni() {
        System.out.println("Introduce o DNI do propietario (* para cancelar) :");
        String dni = scanner.nextLine();
        if (dni.startsWith("*")) return null;
        return dni;
    }    
}
