package concesionariomysql;
import RDB.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PropietariosDAO {
    public static int insertarPropietario(Propietario propietario) throws SQLException {
        String sql = "INSERT INTO Propietarios(dni,nome,apelidos) VALUES(?,?,?);";
        Connection con=DB.getConnection();  // Fora do try con recursos porque non queremos pechar aquí a conexión
        // Si pecharamos a conexión funcionaría todo igual, xa que getConnection() reconecta si é necesario (var DB.java)
        
        try (PreparedStatement stmt = con.prepareStatement(sql);) {
            //agregamos os valores:
            stmt.setString(1, propietario.getDni()); 
            stmt.setString(2, propietario.getNome());
            stmt.setString(3, propietario.getApelidos());
            return stmt.executeUpdate(); //executamos
        } 
    }
    
    public static int updatePropietario(Propietario propietario) throws SQLException {
        String sql = "UPDATE Propietarios SET nome=?, apelidos=? WHERE dni=?;";
        Connection con=DB.getConnection();  // Fora do try con recursos porque non queremos pechar aquí a conexión
        // Si pecharamos a conexión funcionaría todo igual, xa que getConnection() reconecta si é necesario (var DB.java)
        
        try (PreparedStatement stmt = con.prepareStatement(sql);) {
            //agregamos os valores:
            stmt.setString(1, propietario.getNome()); 
            stmt.setString(2, propietario.getApelidos());
            stmt.setString(3, propietario.getDni());
            return stmt.executeUpdate(); //executamos
        }
    }

    public static Propietario recuperarPropietario(String dni) throws SQLException {
        String sql="SELECT * FROM Propietarios P LEFT JOIN Vehiculos V ON  P.dni=V.propietario WHERE P.dni=? ";
        HashMap<String,Propietario> results=new HashMap<>();
        Connection con=DB.getConnection();
        try (PreparedStatement stmt=con.prepareStatement(sql)) {  // Cando se pecha un PreparedStatement, se pecha o ResultSet
            stmt.setString(1,dni);
            ResultSet rs=stmt.executeQuery();
            while(rs.next()) scanResult(results,rs);
        }
        Collection<Propietario> r=results.values();
        if (r.isEmpty()) return null;
        return r.stream().findFirst().get();
    }
    
    public static ArrayList<Propietario> cargarPropietarios() throws SQLException {
        String sql="SELECT * FROM Propietarios P LEFT JOIN Vehiculos V ON  P.dni=V.propietario";
        HashMap<String,Propietario> results=new HashMap<>();
        Connection con=DB.getConnection();
        try (Statement stmt=con.createStatement(); 
            ResultSet rs=stmt.executeQuery(sql);) {
            while(rs.next()) scanResult(results,rs);
        }
        return new ArrayList<>(results.values());
    }
    
    // Si o propietario ten vehiculos o campo propietario de eses vehículos se pon a NULL automáticamente
    // Para iso debemos indicalo no esquema da base de datos en ON DELETE SET NULL cnado creamos a relación.
    public static int eliminaPropietario(Propietario p) throws SQLException {
        return eliminaPropietario(p.getDni());
    }
    
    public static int eliminaPropietario(String dni) throws SQLException {
        String sql = "DELETE FROM Propietarios WHERE dni = ?";

        Connection con=DB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql);) {
            //agregamos os valores:
            stmt.setString(1, dni); //engadimos o value
            return stmt.executeUpdate(); // recollemos o número de eliminados   
        } 
    }

    private static void scanResult(HashMap<String,Propietario> lista,ResultSet row) throws SQLException {
        String dni=row.getString("P.dni");
        // Recuperamos o Propietario do HashMap
        Propietario p=lista.get(dni);
        if (p==null) {  // Non está, o creamos
            p=new Propietario(dni,row.getString("P.nome"),row.getString("P.apelidos"));
            lista.put(dni, p);
        }
        // Engadimos o vehiculo (si existe) ao propietario.
        String matricula=row.getString("V.matricula");
        if (row.getString("V.matricula")!=null) {
            Vehiculo v=new Vehiculo(matricula,row.getString("V.marca"),row.getString("V.modelo"));
            p.addVehiculo(v);
        }
    }
    
}
