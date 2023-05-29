package Database;

/**
 * Database: Define unha API de acceso a datos
 * @author xavi
 * @param <R> - Tipo de datos a xestionar na Base de Datos (Clase do obxecto que queremos xestionar)
 */
public interface Database<R> {
    /**
     * Inserta un novo rexistro na Base de Datos
     * @param data Información a insertar na base de datos
     * @throws DatabaseException  
     * 
     * O rexistro a insertar non debe existir. Si existe, se lanzará unha DatabaseException coa
     * mensaxe "O rexistro xa existe"
     */
    public void insert(R data) throws DatabaseException;
    
    /**
     * Actualiza un rexistro existente na Base de datos
     * @param data Información a Actualizar
     * @throws DatabaseException 
     * 
     * O rexistro a actualizar debe existir. Si non existe, se lanzará unha DatabaseException coa
     * mensaxe "O rexistro non existe"
     */
    public void update(R data) throws DatabaseException;
    
    
    /**
     * Inserta (si non existe) ou actualiza (si existe) un rexistro na base de datos
     * @param data Información a actualizar / insertar
     * @throws DatabaseException 
     */
    public void insertOrUpdate(R data) throws DatabaseException;
    
    /**
     * Elimina un rexistro da base de datos
     * @param data - Rexistro a eliminar
     * @throws DatabaseException 
     * 
     * Lanza DatabaseException coa mensaxe "O rexistro non existe" en caso de non existir
     */
    public void delete(R data) throws DatabaseException;
    
    /**
     * Elimina un rexistro ou varios da bse de datos
     * @param c Criterio que deben cumplir os rexistros a ser eliminados
     * @return Un array coa información dos rexistros eliminados. 
     * @throws DatabaseException 
     * 
     * Si non se elimina ningún rexistro, retornará null en lugar dun array
     */
    public R[] delete(Criteria<R> c) throws DatabaseException;
    
    /**
     * Consulta rexistros na base de datos
     * @param c Criterio que deben cumplir o rexistro
     * @return Un array cos rexistros que cumplen o criterio
     * @throws DatabaseException 
     * 
     * Si ningún rexistro cumple o criterio retornará null.
     */
    public R[] select(Criteria<R> c) throws DatabaseException;
}
