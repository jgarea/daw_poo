package tarea08.clases;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author
 */
public class Concesionario {

    /**
     * La estructura más adecuada en este caso sería un TreeSet, ya que con Set evitamos que haya duplicados y con Tree los almacenaremos ordenados.
     */
    final static int TAM_CONCESIONARIO = 50;
    Set<Vehiculo> concesionario;
    
    int numVehiculos;

    public Concesionario() {
        concesionario = new TreeSet<>();
        numVehiculos = 0;
    }

    public String buscaVehiculo(String matricula) {
        Iterator<Vehiculo> it =concesionario.iterator();
        Vehiculo v;
        while(it.hasNext()){
            v=it.next();
            if(v.getMatricula().equals(matricula)){
                return v.getMarca() + " " + v.getMatricula() + " " + v.getPrecio();
            }
        }
        
        return null;
    }
        
    public int insertarVehiculo(String marca, String matricula, int numkms, LocalDate fecha_mat, String descripcion, int precio, String propietario, String dni_propietario) {
        if (numVehiculos >= TAM_CONCESIONARIO) 
            return -1;
        else if (this.buscaVehiculo(matricula)!=null)
            return -2;
        else {
            concesionario.add(new Vehiculo(marca, matricula, numkms, fecha_mat, descripcion, precio, propietario, dni_propietario));
            numVehiculos++;
        }
        
        return 0;
    }
    
    public void listaVehiculos (){
        concesionario.forEach((t) -> System.out.println ("Vehículo:" + t.getMarca() + " Matrícula: " + t.getMatricula() + " Precio: " + t.getPrecio() + " Descripción: " + t.getDescripcion()));
    }
    
    public boolean actualiza_kmVeh (String matricula, int kms){
        Iterator<Vehiculo> it =concesionario.iterator();
        Vehiculo v;
        while(it.hasNext()){
            v=it.next();
            if(v.getMatricula().equals(matricula)){
                v.setNum_kms(kms);
                return true;
            }
        }
        return false;
        
    }
    public boolean eliminarVehiculo(String matricula){
        Iterator<Vehiculo> it =concesionario.iterator();
        Vehiculo v;
        while(it.hasNext()){
            v=it.next();
            if(v.getMatricula().equals(matricula)){
                it.remove();
                numVehiculos--;
                return true;
            }
        }
        return false;
    }
    
}
