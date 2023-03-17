/**
 *
 * @author Juan
 */
public class ArtigoBridge {
    private Formatter artigo;

    public ArtigoBridge(Formatter artigo) {
        this.artigo = artigo;
    }

  
    public String format(ArtigoBridge o){
        return artigo.format(o);
    }
    
   public double getPrezo(){
       ArtigoFormatter a=(ArtigoFormatter)artigo;
       return a.getPrezo();
   }
    
}
