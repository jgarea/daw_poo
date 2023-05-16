package concesionarioimporter;

import RDB.DB;
import concesionariomysql.Propietario;
import concesionariomysql.PropietariosDAO;
import concesionariomysql.Vehiculo;
import concesionariomysql.VehiculosDAO;
import java.io.IOException;
import java.sql.SQLException;

public class ConcesionarioImporter {

    public static void main(String[] args) throws IOException {
        DB.URL="jdbc:mariadb://192.168.122.2:3306/Concesionario?allowPublicKeyRetrieval=true&useSSL=false";
        DB.USER="concesionario";
        DB.PASS="concesionario";
        try (DB db=DB.open()) {
            System.out.println("Importando Datos");
            importCSV();
            System.out.println("Datos Importados");
        } catch (SQLException ex) {
            System.out.println("Erro na base de datos: "+ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro cargando driver da base de datos: "+ex.getMessage());
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void importCSV() throws IOException, SQLException {
        String ppath=ConcesionarioImporter.class.getResource("/resources/Propietarios.csv").getPath();        
        String vpath=ConcesionarioImporter.class.getResource("/resources/Vehiculos.csv").getPath();
        
        /***
         * Tres xeitos distintos de ler os vehiculos e propietarios.
         * Poden utilizarse calquera dos obxectos CSVReader incluso
         * mezclando tipos, mentras teñamos un vreader e un preader.
         */
        
        
        //CSVReader<Vehiculo> vreader=new VehiculoCSVReaderRAS(vpath);
        //CSVReader<Propietario> preader=new PropietarioCSVReaderRAS(ppath);
        
        //CSVReader<Vehiculo> vreader=new VehiculoCSVReaderStreams(vpath);
        //CSVReader<Propietario> preader=new PropietarioCSVReaderStreams(ppath);
        
        CSVReader<Vehiculo> vreader=new VehiculoCSVReader(vpath);
        CSVReader<Propietario> preader=new PropietarioCSVReader(ppath);
        String[] listaPropietarios=preader.readCSV();
        String[] listaVehiculos=vreader.readCSV();
        for(int idx=1;idx<listaPropietarios.length;idx++) {
            PropietariosDAO.insertarPropietario(preader.stringToObject(listaPropietarios[idx]));
        }
        for(int idx=1;idx<listaVehiculos.length;idx++) {
            Propietario p=null;
            String l=listaVehiculos[idx];
            Vehiculo v=vreader.stringToObject(l);
            String[] fields=l.split(",");
            if (fields.length==4) {
                System.out.println(String.join(",", fields));
                p=preader.getObject(Integer.parseInt(fields[3]));
                System.out.println("Propietario: "+p);
            }
            v.setPropietario(p);
            VehiculosDAO.insertarVehiculo(v);
        }
    }
    
}
