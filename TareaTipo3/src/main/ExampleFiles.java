package main;

import tipo3.Storage;
import tipo3.Usuario;
import tipo3.UsuariosStorageFile;

public class ExampleFiles {
    public static void main(String[] args) {
        try(Storage storage=new UsuariosStorageFile("resultados.txt","resultados.idx")) {    
            Example example=new Example(storage);
            example.aplicacion();
        } catch (Exception ex) {
           System.out.println("Error: "+ex.getMessage());
        }
    }    
}
