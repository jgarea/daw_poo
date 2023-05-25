package tipo3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public final class UsuariosStorageDDBB implements Storage {
   
    private Usuario getUsuario(ResultSet rs) throws SQLException {
        return new Usuario(rs.getString("dni"),rs.getString("nome"),rs.getInt("edade"));
    }
    
    @Override
    public Usuario get(String dni) throws Exception {
        String sql="SELECT * FROM usuarios WHERE dni=?";
        Connection con=DB.getConnection();
        try(PreparedStatement stmt=con.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs=stmt.executeQuery();
            if (rs.next()) return getUsuario(rs);
            return null;
        }
    }

    @Override
    public void insert(Usuario user) throws Exception {
        String sql="INSERT INTO usuarios(dni,nome,edade) VALUES(?,?,?)";
        Connection con=DB.getConnection();
        try(PreparedStatement stmt=con.prepareStatement(sql)) {
            stmt.setString(1, user.getDni());
            stmt.setString(2, user.getNome());
            stmt.setInt(3, user.getIdade());
            stmt.executeUpdate();
        }    
    }

    @Override
    public void update(Usuario user) throws Exception {
        String sql="UPDATE usuarios SET nome=?,edade=? WHERE dni=?";
        Connection con=DB.getConnection();
        try(PreparedStatement stmt=con.prepareStatement(sql)) {
            stmt.setString(3, user.getDni());
            stmt.setString(1, user.getNome());
            stmt.setInt(2, user.getIdade());
            stmt.executeUpdate();
        } 
    }

    @Override
    public void delete(String dni) throws Exception {
        String sql="DELETE FROM usuarios WHERE dni=?";
        Connection con=DB.getConnection();
        try(PreparedStatement stmt=con.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.executeUpdate();
        } 
    }

    @Override
    public void close() throws Exception {
    }
    
}
