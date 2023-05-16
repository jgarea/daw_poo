package concesionariomysql;

import RDB.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class VehiculosDAO {
    public static int insertarVehiculo(Vehiculo vehiculo) throws SQLException{
        String dnipropietario=null;
        String sql = "INSERT INTO Vehiculos(matricula,marca,modelo,propietario) VALUES(?,?,?,?);";
        Connection con=DB.getConnection();  // Fora do try con recursos porque non queremos pechar aquí a conexión
        // Si pecharamos a conexión funcionaría todo igual, xa que getConnection() reconecta si é necesario (var DB.java)
        Propietario p=vehiculo.getPropietario();
        if (p!=null) dnipropietario=p.getDni();
        try (PreparedStatement stmt = con.prepareStatement(sql);) {
            //agregamos os valores na consulta:
            stmt.setString(1,vehiculo.getMatricula()); //engadimos o primeiro value
            stmt.setString(2,vehiculo.getMarca());
            stmt.setString(3,vehiculo.getModelo());//engadimos o segundo value
            stmt.setString(4, dnipropietario);
            return stmt.executeUpdate(); //executamos
        } 
    }
    
    public static int actualizarPropietarioVehiculo(Vehiculo v, Propietario p) throws SQLException {
        int ret=actualizarPropietarioVehiculo(v.getMatricula(),p.getDni());
        if (ret==1) v.setPropietario(p);
        return ret;
    }
    
    public static int actualizarPropietarioVehiculo(String matricula, Propietario p) throws SQLException{
        return actualizarPropietarioVehiculo(matricula,p.getDni());
    }
    
    public static int actualizarPropietarioVehiculo(String matricula, String dnipropietario) throws SQLException{
        String sql = "UPDATE Vehiculos SET propietario = ? WHERE matricula = ?;";
        Connection con=DB.getConnection();
        
        try(PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1,dnipropietario);
            stmt.setString(2, matricula);
            return stmt.executeUpdate(); // recollemos o número de modificacions   
        } 
    }
    
    public static int actualizarVehiculo(Vehiculo v) throws SQLException{
        String sql = "UPDATE Vehiculos SET marca= ?,modelo= ?,propietario = ? WHERE matricula = ?;";
        Connection con=DB.getConnection();
        
        try(PreparedStatement stmt = con.prepareStatement(sql);) {
            Propietario p=v.getPropietario();
            String dni=null;
            if (p!=null) dni=p.getDni();
            
            stmt.setString(1,v.getMarca());
            stmt.setString(2,v.getModelo());
            stmt.setString(3,dni);
            stmt.setString(4, v.getMatricula());
            return stmt.executeUpdate(); // recollemos o número de modificacions   
        } 
    }

    public static int eliminarVehiculo(Vehiculo v) throws SQLException{
        return eliminarVehiculo(v.getMatricula());
    }
    
    public static int eliminarVehiculo(String matricula) throws SQLException{
        String sql = "DELETE FROM Vehiculos WHERE matricula = ?";
        Connection con=DB.getConnection();

        try(PreparedStatement stmt = con.prepareStatement(sql);) {
            stmt.setString(1,matricula);
            return stmt.executeUpdate(); 
        } 
    }
    
     public static Vehiculo recuperarVehiculo(String matricula) throws SQLException{
        String sql="SELECT * FROM Vehiculos V LEFT JOIN Propietarios P ON  P.dni=V.propietario WHERE matricula = ?";
        Connection con=DB.getConnection();
        Vehiculo v=null;
        try (PreparedStatement stmt=con.prepareStatement(sql);) { 
            stmt.setString(1,matricula);
            ResultSet row=stmt.executeQuery();
            if (row.next()) v=parseRow(row);
        }
        return v;
    }

       
    public static ArrayList<Vehiculo> recuperarVehiculos() throws SQLException{
        return recuperarVehiculos(null);
    } 
     
    public static ArrayList<Vehiculo> recuperarVehiculos(String marca) throws SQLException{
        ArrayList<Vehiculo> lista=new ArrayList<>();
        String sql = "SELECT * FROM Vehiculos V LEFT JOIN Propietarios P ON  P.dni=V.propietario";
        if (marca!=null) sql+=" WHERE marca = ?;";
        Connection con=DB.getConnection();
        try (PreparedStatement stmt=con.prepareStatement(sql);) { 
            if (marca!=null) stmt.setString(1,marca);
            ResultSet row=stmt.executeQuery();
            while(row.next()) lista.add(parseRow(row));
        }
        return lista;
    }
    
    private static Vehiculo parseRow(ResultSet row) throws SQLException {
        Vehiculo v=new Vehiculo(row.getString("matricula"),row.getString("marca"),row.getString("modelo"));
        String dni=row.getString("propietario");
        if (dni!=null) {
            Propietario p=new Propietario(dni,row.getString("nome"),row.getString("apelidos"));
            p.addVehiculo(v);
        }
        return v;
    }
}
