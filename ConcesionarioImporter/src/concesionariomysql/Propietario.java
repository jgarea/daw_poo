package concesionariomysql;

import java.util.ArrayList;
import java.util.HashSet;

public class Propietario {
    private String dni;
    private String nome;
    private String apelidos;
    private HashSet<Vehiculo> vehiculos;

    public Propietario(String dni, String nome, String apelidos) {
        this.dni = dni;
        this.nome = nome;
        this.apelidos = apelidos;
        this.vehiculos = new HashSet<>();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelidos() {
        return apelidos;
    }

    public void setApelidos(String apelidos) {
        this.apelidos = apelidos;
    }
    
    public Vehiculo[] getVehiculos() {
        System.out.println("Obteniendo vehiculos del propietario....");
        return vehiculos.toArray(new Vehiculo[0]);
    }
    
    public void addVehiculo(Vehiculo v) {
        v.setPropietario(this);
        vehiculos.add(v);
    }

    @Override
    public String toString() {
        return "Propietario{" + "dni=" + dni + ", nome=" + nome + ", apelidos=" + apelidos + '}';
    }
}
