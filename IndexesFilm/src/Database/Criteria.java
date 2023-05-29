package Database;

public interface Criteria<R> {
    // Devolve true si o parámetro r resulta "aceptable" segundo a Criteria elexida.
    public boolean accept(R r);
}
