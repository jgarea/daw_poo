/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tipo3;

import java.util.Objects;

/**
 *
 * @author xavi
 */
public class Usuario {
    private final String dni;
    private String nome;
    private int idade;

    
    public static Usuario fromString(String line) throws Exception {
        String[] fields=line.split(",");
        // Aquí poderíamos verificar a corrección dos datos lanzando unha Exception en caso de erro
        return new Usuario(fields[0].trim(),fields[1].trim(),Integer.parseInt(fields[2])); // Aquí usaríamos os datos correspondentes ao dni, nome e idade
    }
    
    public Usuario(String dni, String nome, int idade) {
        this.dni = dni;
        this.nome = nome;
        this.idade = idade;
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

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public String toString() {
        return "Usuario{" + "dni=" + dni + ", nome=" + nome + ", idade=" + idade + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.dni, other.dni)) {
            return false;
        }
        return true;
    }
   
}
