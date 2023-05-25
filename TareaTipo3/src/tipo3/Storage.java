package tipo3;


public interface Storage extends AutoCloseable {
    public Usuario get(String dni)  throws Exception;  // Retorna todos os traballadores ordeados por DNI de menor a maior
    public void insert(Usuario user) throws Exception; 
    public void update(Usuario user) throws Exception;
    public void delete(String dni) throws Exception;
}
