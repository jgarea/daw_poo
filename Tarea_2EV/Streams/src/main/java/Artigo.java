
import java.util.Objects;

/**
 *
 * @author Juan
 */
public class Artigo implements Comparable<Artigo>{
    private String codigo;
    private String denominacion;
    private double prezo;

    public Artigo(String codigo, String denominacion, double prezo) {
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.prezo = prezo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public double getPrezo() {
        return prezo;
    }

    public void setPrezo(double prezo) {
        this.prezo = prezo;
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
        final Artigo other = (Artigo) obj;
        return Objects.equals(this.codigo, other.codigo);
    }

    @Override
    public int compareTo(Artigo o) {
        if(this.equals(o))
            return 0;
        return denominacion.compareTo(o.getDenominacion());
    }
    
}
