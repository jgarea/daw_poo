package concesionariocsv;

import database.DatabaseException;
import java.io.IOError;
import java.io.IOException;
import java.io.RandomAccessFile;

public class VehiculoDatabase extends CSVIndexedDatabase<String,Vehiculo> {

    public VehiculoDatabase(String filename) {
        super(filename);
    }

    // Devolve a chave primaria do Vehículo (matricula)
    @Override
    public String getKey(Vehiculo data) {
        return data.getMatricula();
    }

    // Marca como eliminado o Vehiculo. Entendemos que está eliminado si a matrícula comeza con *
    @Override
    protected void remove(RandomAccessFile ras) throws DatabaseException {
        try {
            ras.write("*".getBytes()); // Escribo os Bytes para gardar a representación UTF-8 correctamente
        } catch (IOException ex) {
           throw new DatabaseException("ERRO remove Vehiculo: "+ex.getMessage());
        }
    }

    // Retorna o obxecto Vehiculo representado polo String do CSV
    @Override
    protected Vehiculo stringToObject(String line) throws DatabaseException {
        Propietario propietario=null;
        String[] fields=line.split(",");
        if (fields.length==4) { // O Vehículo ten propietario
            int nline=Integer.parseInt(fields[3].trim());
            propietario=ConcesionarioCSV.getPropietario(nline);  // Recuperamos o propietario do Vehículo
        }
        if (fields.length<3 || fields.length>4) 
            throw new IOError(new DatabaseException("O ficheiro de Vehículos está corrupto: "+line));
        return new Vehiculo(fields[0],fields[1],fields[2],propietario);    
    }

    // Retorna un String CSV representando a información do Vehículo
    @Override
    protected String getCSVLine(Vehiculo data) throws DatabaseException {
        String vline=data.getMatricula()+","+data.getMarca()+","+data.getModelo()+",";
        Propietario p=data.getPropietario();
        if (p!=null) { // O Vehiculo ten propietario
            int pline=ConcesionarioCSV.getLine(data.getPropietario()); // Obtemos a liña do CSV onde se atopa o propietario
            if (pline>0) vline+=pline;
        }    
        return vline;
    }

    // Indica si un Vehiculo está "borrado" (a matricula comeza por *)
    @Override
    protected boolean isDeleted(Vehiculo data) {
        return data.getMatricula().startsWith("*");
    }
}
