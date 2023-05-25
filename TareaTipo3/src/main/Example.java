package main;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import tipo3.GenericImporter;
import tipo3.Importer;
import tipo3.Storage;
import tipo3.Usuario;

/**
 * Para non repetir código, fago esta clase que recibe o tipo de Storage desexado
 * @author xavi
 */
public class Example {
    private final Storage storage;
    private final Importer importer;
    private final Scanner scn=new Scanner(System.in);
    
    public Example(Storage storage) {
        this.storage=storage;
        this.importer=new GenericImporter(storage);
    }
    
    public void aplicacion() {
        Usuario user;
        String dni,nome,edade;
        int anos;
        
        int op=0;
        do {
            System.out.println("Menu Principal");
            System.out.println("1.- Importación de Datos");
            System.out.println("2.- Busca/Modificación/Baixa de Usuario");
            System.out.println("3.- Alta de Usuario");
            System.out.println("4.- Saír");
            System.out.print("\nElixe Opcion: ");
            try {
                op=Integer.parseInt(scn.nextLine());
                switch(op) {
                case 1:
                    importer.importData();
                    System.out.println("Datos importados");
                    break;
                case 2:
                    System.out.print("DNI: ");
                    dni=scn.nextLine();
                    user=storage.get(dni);
                    if (user==null) System.out.println("O usuario con dni "+dni+" non existe ");
                    else {
                        System.out.println(user);
                        System.out.println("Desexas (B)orrar, (M)odificar ou (C)ancelar?");
                        String l=scn.nextLine();
                        if (l.length() > 0) {
                            switch(l.toUpperCase().charAt(0)) {
                                case 'B':
                                    storage.delete(dni);
                                    System.out.println("Usuario eliminado.");
                                    break;
                                case 'M':
                                    boolean modif=false;
                                    System.out.print("NOME ["+user.getNome()+"]:");
                                    nome=scn.nextLine();
                                    if ((nome.length()!=0)&&(!nome.equals(user.getNome()))) {
                                        user.setNome(nome);
                                        modif=true;
                                    }
                                    System.out.print("EDADE ["+user.getIdade()+"]:");
                                    edade=scn.nextLine();
                                    if (edade.length()!=0) {
                                        anos=Integer.parseInt(edade);
                                        if (anos!=user.getIdade()) {
                                            user.setIdade(anos);
                                            modif=true;
                                        }
                                    }
                                    if (modif) {
                                        storage.update(user);
                                        System.out.println("Usuario actualizado.");
                                    }
                                    break;
                            }
                         }
                    }
                    break;
                case 3:
                    System.out.print("DNI (Enter para cancelar): ");
                    dni=scn.nextLine();
                    if (dni.length()>0) {
                        user=storage.get(dni);
                        if (user!=null) System.out.println("Xa existe: "+user);
                        else {
                            System.out.print("NOME (Enter para cancelar): ");
                            nome=scn.nextLine();
                            if (nome.length()>0) {
                                System.out.print("IDADE (Enter para cancelar): ");
                                edade=scn.nextLine();
                                if (edade.length()>0) {
                                    anos=Integer.parseInt(edade);
                                    user=new Usuario(dni,nome,anos);
                                    storage.insert(user);
                                    System.out.println("Usuario insertado.");
                                }
                            }
                        }
                    }
                    break;
            }
        } catch(NumberFormatException e) {
        } catch (Exception ex) {
                Logger.getLogger(Example.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        } while(op!=4);
    }
}
