/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Juan
 */
public class ArtigoFormatter extends Artigo implements Formatter<Artigo>{

    public ArtigoFormatter(String codigo, String denominacion, double prezo) {
        super(codigo, denominacion, prezo);
    }


    @Override
    public String format(Artigo o) {
        return "Codigo "+o.getCodigo()+" Denominacion "+o.getDenominacion()+" Prezo "+o.getPrezo();
    }
    
}
