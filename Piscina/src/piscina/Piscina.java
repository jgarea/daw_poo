/*
Unha piscina dispón dun número de socios que poden facer uso das instalacións. 
A información relevante dos socios é o seu dni, o nome, os apelidos, o teléfono e o seu email.

Se desexa facer un programa Java que permita:

1.- Dar de alta socios

2.- Eliminar socios

3.- Listar socios en pantalla

4.- Crear un ficheiro  CSV  "socios.csv" cos datos dos socios.

Os datos deben ser persistentes, de xeito que cando se crean os socios se conserven entre unha execución do programa e outra.

Se pide implementar a solución de dous xeitos: 
 */
package piscina;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author xavi
 */
public class Piscina {
    static Scanner scn=new Scanner(System.in);
    
    public static void altaSocio(Database db) {
        Socio socio=null;
        try {
            String dni,nome,apelidos,telefono,email;

            System.out.print("DNI: ");
            dni=scn.nextLine().replace(","," ");
            System.out.print("Nome: ");
            nome=scn.nextLine().replace(","," ");
            System.out.print("Apelidos: ");
            apelidos=scn.nextLine().replace(","," ");
            System.out.print("Telefono: ");
            telefono=scn.nextLine().replace(","," ");
            System.out.print("E-mail: ");
            email=scn.nextLine().replace(","," ");
            socio=new Socio(dni,nome,apelidos,telefono,email);
            db.save(socio);
        } catch(DatabaseException e) {
            System.out.println("Erro dando alta ao socio: "+socio);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int op=0;
                
        try (Database<Socio> db=new RandomAccessFileDatabase() {
            @Override
            protected Textualizable buildObject() {
                return new Socio();
            
            }}) {
            do {
                try {
                    System.out.println("Menu Prncipal");
                    System.out.println("1.- Alta de Socios");
                    System.out.println("2.- Baixa de Socios");
                    System.out.println("3.- Listado de Socios");
                    System.out.println("4.- Obter CSV");
                    System.out.println("5.- Sair");
                    System.out.print("\nElixe Opcion: ");
                    op=Integer.parseInt(scn.nextLine());
                    switch(op) {
                        case 1:
                            altaSocio(db);
                            break;
                        case 2:
                            System.out.print("DNI: ");
                            String dni=scn.nextLine();
                            Socio s=new Socio();
                            s.setDni(dni);
                            db.delete(s);
                            break;
                        case 3:
                            System.out.println("Listado de Socios");
                            for(Socio socio:db) {
                                System.out.println(socio);
                            }
                            break;
                        case 4:
                            System.out.println("Nome do ficheiro: ");
                            String filename=scn.nextLine();
                            try (PrintWriter pw=new PrintWriter(filename)) {
                               pw.println("dni,nome,apelidos,telefono,email");
                               for(Socio socio:db) {
                                   pw.println(socio.toText());
                               }
                            }
                            break;
                    }
                } catch(NumberFormatException e) {
                    System.out.println("Debes introducir un número entre 1 e 5");
                }
            } while(op!=5);
        } catch(Exception e) {
            System.out.println("ERROR: "+e.getMessage());
        }
    }
}
