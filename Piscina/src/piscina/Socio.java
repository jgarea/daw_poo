package piscina;

import java.util.Objects;

/**
 *
 * @author xavi
 */
class Socio implements Textualizable {
    private String dni;
    private String nome;
    private String apelidos;
    private String telefono;
    private String email;

    public Socio() {}
    
    public Socio(String dni, String nome, String apelidos, String telefono, String email) {
        this.dni = dni;
        this.nome = nome;
        this.apelidos = apelidos;
        this.telefono = telefono;
        this.email = email;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "[" + dni + "] " + nome + " " + apelidos;
    }

    @Override
    public String toText() {
        return dni+","+nome+","+apelidos+","+telefono+","+email;
    }

    @Override
    public void fromText(String data) throws Exception {
        String[] f=data.split(",");
        if (f.length!=5) throw new Exception("Datos erróneos: "+data);
        setDni(f[0]);
        setNome(f[1]);
        setApelidos(f[2]);
        setTelefono(f[3]);
        setEmail(f[4]);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.dni);
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
        final Socio other = (Socio) obj;
        if (!Objects.equals(this.dni, other.dni)) {
            return false;
        }
        return true;
    }
    
}
