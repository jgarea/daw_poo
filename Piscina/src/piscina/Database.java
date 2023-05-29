/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piscina;

/**
 *
 * @author xavi
 */
interface Database<T extends Textualizable> extends AutoCloseable, Iterable<T> {

    // Garda os datos si non existen, si existen lanza DatabaseException
    public void save(T data) throws DatabaseException;
    // Modifica os datos si existen, si non existen lanza DatabaseException
    public void update(T data) throws DatabaseException;
    // Elimina datos. En caso de erro lanza DatabaseException
    public void delete(T data) throws DatabaseException;
    // Busca os datos correspondentes coa información almacenada en data e os pon en data, Retorna 
    // data ou null si non ten información.
    public T get(T data) throws DatabaseException;
    
}
