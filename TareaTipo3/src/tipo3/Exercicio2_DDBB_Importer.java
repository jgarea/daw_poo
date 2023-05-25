package tipo3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Exercicio2_DDBB_Importer implements Importer {
    // Eficiente ao utilizar unha sentencia preparada e non depende de UsuariosStorageDDBB
    //
    public void importData() {
        String sql="INSERT INTO usuarios(dni,nome,edade) VALUES(?,?,?)";
        Connection con=DB.getConnection();
   
        try (BufferedReader rdr=new BufferedReader(new FileReader("datos.txt"));
             PreparedStatement stmt=con.prepareStatement(sql)) {
            String line=rdr.readLine();
            // Si fora un CSV, deberíamos saltamos esta liña (a cabeceira) facendo outro rdr.readLine()
            while(line!=null) {
                Usuario user=Usuario.fromString(line);
                stmt.setString(1, user.getDni());
                stmt.setString(2, user.getNome());
                stmt.setInt(3, user.getIdade());
                stmt.executeUpdate();
                System.out.println("Importado: "+user);
                line=rdr.readLine();
            }
        } catch(Exception ex) {
            System.out.println("Error: "+ex.getMessage());
        }
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Exercicio2_DDBB_Importer importer=new Exercicio2_DDBB_Importer();
        DB.URL="jdbc:mariadb://192.168.122.2:3306/mi_bd?allowPublicKeyRetrieval=true&useSSL=false";
        DB.USER="user";
        DB.PASS="pass";
        try(DB db=DB.open()) {
            importer.importData();
        }
    }
}
