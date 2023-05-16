package RDB;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author xavi
 */
public class DB  implements Closeable {
    private static DB db=null;
    public static String DRIVER="org.mariadb.jdbc.Driver";
    public static String URL="jdbc:.....";
    public static String USER="user";
    public static String PASS="pass";
    
    private Connection conn=null;
        
    /**
     *  Singleton. open crea o obxeto DB que establece a conexión
     *  si non está xa establecida. Si está establecida retorna o obxecto
     *  existente. Iso evita facer conexións/desconexións continuas.
     */
    public static DB open() throws SQLException, ClassNotFoundException {
        if (DB.db==null) DB.db=new DB();
        return DB.db;
    }
    
   /**
    * Retorna a conexión para traballar coa BBDD
    */
    public static Connection getConnection() {
        if (DB.db==null) throw new RuntimeException("Debes abrir antes a conexión con open");
        try {
            if (DB.db.conn==null) DB.db.connect();  // Reconectamos si é necesario
            return DB.db.conn;
        } catch (ClassNotFoundException | RuntimeException | SQLException ex) {
            throw new RuntimeException("Erro obtendo conexión coa BBDD: "+ex.getMessage());
        }
    }

    /**
     * Construtor. É privado para implementer Singleton. Garante que unicamente
     * pode existir unha instancia da clase
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    private DB() throws SQLException, ClassNotFoundException {
        connect();
    }
    
    /**
     * Establece a conexión coa BBDD
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    private final void connect() throws SQLException,ClassNotFoundException {
        Class.forName(DB.DRIVER);
        conn=DriverManager.getConnection(DB.URL,DB.USER,DB.PASS);
    }
    
    /**
     * O método close de Closeable non pode lanzar SQLException,lanzo unha non-checked exception
     * en caso de erro.
     */
    @Override
    public void close() {
        if (conn!=null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro pechando conexión: "+ex.getMessage());
            }
        }
        conn=null;
        DB.db=null;
    }
}
