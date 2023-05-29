package piscina;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author xavi
 */
public class SRandomAccessFileDatabase extends RandomAccessFileDatabase<Socio> {

    public SRandomAccessFileDatabase() throws FileNotFoundException,IOException {
        super();
    }
    
    @Override
    protected Socio buildObject() {
        return new Socio();
    }
    
}
