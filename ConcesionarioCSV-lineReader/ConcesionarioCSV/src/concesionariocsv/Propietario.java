package concesionariocsv;

import java.util.Objects;


public class Propietario {
    private final String dni;
    private String nome;
    private String apelidos;

    public Propietario(String dni, String nome, String apelidos) {
        this.dni = dni;
        this.nome = nome;
        this.apelidos = apelidos;
    }

    public String getDni() {
        return dni;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.dni);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Propietario other = (Propietario) obj;
        if (!Objects.equals(this.dni, other.dni)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Propietario{" + "dni=" + dni + ", nome=" + nome + ", apelidos=" + apelidos + '}';
    }
    
    
}
