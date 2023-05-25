package tipo3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;

/**
 * Outra opción:
 * 
 * Importa os datos ao ficheiro resultados.txt ou a BBDD dependendo
 * do Storage que se lle pase ao construtor.
 * 
 */
public class GenericImporter implements Importer {
    private final Storage storage;
    
    public GenericImporter(Storage storage) {
        this.storage=storage;
    }

    @Override
    public void importData() {
        try (BufferedReader rdr=new BufferedReader(new FileReader("datos.txt"))) {
            String line=rdr.readLine();
            // Si fora un CSV, deberíamos saltamos esta liña (a cabeceira) facendo outro rdr.readLine()
            while((line!=null)&&(line.length()>0)) {
                Usuario user=Usuario.fromString(line);
                storage.insert(user);
                System.out.println("Importado: "+user);
                line=rdr.readLine();
            }
        } catch(Exception ex) {
            System.out.println("Error: "+ex.getMessage());
        }
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DB.URL="jdbc:mariadb://192.168.122.2:3306/mi_bd?allowPublicKeyRetrieval=true&useSSL=false";
        DB.USER="user";
        DB.PASS="pass";
        /**
         * Si creamos un UsuariosStorageDDBB a "importacion" se fai a unha BBDD relacional
         * Si creamos un UsuariosStorageFile a "importacion" se fai a ficheiros de acceso aleatorio cun indice
         */
        try(
            DB db=DB.open();
            //Storage storage=new UsuariosStorageDDBB()
              Storage storage=new UsuariosStorageFile("resultados.txt","resultados.idx")
        ) {
            Importer importer=new GenericImporter(storage);
            importer.importData();
        } catch(Exception e) {
            System.out.println("ERROR: "+e.getMessage());
        }
    }
}
