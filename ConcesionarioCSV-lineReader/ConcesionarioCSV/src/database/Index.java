package database;

import java.util.Collection;
import java.util.TreeMap;

public class Index<K> {
    private final TreeMap<K,Long> index=new TreeMap<>();
 
    public void addIndex(K key,long position) {
        index.put(key,position);
    }
    
    public void delIndex(K key) {
        index.remove(key);
    }
    
    
    public Collection<Long> getPositions() {
        return index.values();
    }
    /**
     * Retorna a posición na que se atopa o rexistro con chave Key
     * @param key chave do rexistro
     * @return Posición ou null si non está
     * 
     * Temos que retornar a clase Long e non o tipo primitivo long
     * para poder retornar null en caso de que non exista
     */
    public Long getPosition(K key) {
        return index.get(key);
    }
}
