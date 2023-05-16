package database;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Permite manipular un ficheiro de texto composto de liñas sen necesidade de
 * almacenar todas as liñas en memoria.
 */
public class LineReader implements Iterable<String> {
    private final String filename;      // Nome do Ficheiro
    private String header;              // A "cabeceira" é a primeira liña do ficheiro
    private Path path;                  // Path correspondente co nome do ficheiro
    private ArrayList<Long> linepositions;  // Posición de cada liña dentro do ficheiro para poder lela directamente
    
    /**
     * Construtor.
     * @param filename 
     */
    public LineReader(String filename) {
        this.filename=filename;
        this.path=path=FileSystems.getDefault().getPath(filename);
        this.header=null;
        this.linepositions=null;
    }
    
    /**
     * Elimina o índice de xeito que se teña que reconstruír cando se necesite
     */
    public void reset() {
        this.linepositions=null;
    }
  
    /**
     * Engade unha liña ao final do ficheiro actualizando o índice
     * @param line
     * @throws IOException 
     */
    public void addLine(String line) throws IOException {
        if (linepositions==null) linepositions=indexLines();
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            ras.seek(ras.length());
            long position=ras.getFilePointer();
            ras.write(line.getBytes());
            linepositions.add(position);
        } 
    }
    
    /**
     * Sobreescribe o inicio da liña idx con str
     * @param idx
     * @param str
     * @return
     * @throws IOException 
     */
    public String mark(int idx,String str) throws IOException {
        String line=null;
        if (linepositions==null) linepositions=indexLines();
        if ((idx > 0) && (idx<linepositions.size())) {
            try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
                ras.seek(linepositions.get(idx));
                line=readLine(ras);
                
                // Sobreescribimos bytes, non todas as letras ocupan o mesmo nun String
                // Si nos ciñeramos ao alfabeto en inglés non teríamos estes problemas....
                /*
                    UTF-8 utiliza 1 byte para representar caracteres en el set ASCII, 
                    dos bytes para caracteres en otros bloques alfabéticos y tres bytes para el resto 
                */
                byte[] original=line.getBytes();
                byte[] mark=str.getBytes();
                // Potencialmente pode quedar "basura" detrás da marca.
                for(idx=0;idx<mark.length && idx<original.length;idx++) original[idx]=mark[idx];
                ras.seek(linepositions.get(idx));
                ras.write(original);
            } 
        }
        return line;
    }
    
    /**
     * Mira si se engadiron liñas ao ficheiro e as engade ao índice
     *      Miramos a lonxitude do ficheiro
     *      Nos Poñemos na última posición do indice
     *      Lemos a última liña do indice
     *      Mentres a posición actual sexa distinta da lonxitude do ficheiro:
     *          engadimos a posición ao índice
     *          lemos liña
     * 
     * @throws IOException 
     */
    public void refresh() throws IOException {
        if (linepositions==null) linepositions=indexLines();
        else {
            try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
                long length=ras.length();
                long lastpos=linepositions.get(linepositions.size()-1);
                ras.seek(lastpos);
                readLine(ras);
                lastpos=ras.getFilePointer();
                while (lastpos!=length) {
                    linepositions.add(ras.getFilePointer());
                    readLine(ras);
                }
            } 
        }
    }
    
    /**
     * Retorna a liña que ocupa a posición idx. A posición 0 é a cabeceira
     * @param idx
     * @return
     * @throws IOException 
     */
    public String getLine(int idx) throws IOException {
        if (linepositions==null) linepositions=indexLines();
        if ((idx >= 0) && (idx<linepositions.size())) {
            try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
                ras.seek(linepositions.get(idx));
                return readLine(ras);
            } 
        }
        return null;
    }
    
    /**
     * Retorna todas as liñas do ficheiro nun array
     * @return
     * @throws IOException 
     */
    public String[] readAll() throws IOException {
        String[] l=Files.readAllLines(path).toArray(new String[0]);
        if (l.length > 0) {
            this.header=l[0];
            return l;
        }
        return null;
    } 
    
    /**
     * Retorna a primeira liña do ficheiro
     * @return
     * @throws IOException 
     */
    public String getHeader() throws IOException {
        // Si non a temos, a lemos. Se non, simplemente a retornamos
        if (this.header==null) {
            try(BufferedReader reader=new BufferedReader(new FileReader(filename))) {
                this.header=reader.readLine();
            }
        }
        return this.header;
    }
    
    /**
     * Permite ir percorrendo as liñas secuencialmente cun iterador sen
     * necesidade de almacenalas en memoria e con rapidez.
     * @return 
     */
    public Iterator<String> iterator() {
        try {
            if (linepositions==null) linepositions=indexLines();
            return new Iterator<>() {
                int idx=1;
                
                @Override
                public boolean hasNext() {
                    return idx<linepositions.size();
                }
                
                @Override
                public String next() {
                    try {
                        if (idx<linepositions.size()) return getLine(idx);
                    } catch (IOException ex) {
                    }
                    throw new NoSuchElementException();
                }
            };
        } catch (IOException ex) {
            return null;
        }
    }
    
    /**
     * Crea un índice que indica a posición de comenzo de cada liña no ficheiro
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private ArrayList<Long> indexLines() throws FileNotFoundException, IOException {
        ArrayList<Long> indexes=new ArrayList<>();
        try (RandomAccessFile ras=new RandomAccessFile(filename,"rws")) {
            long position=ras.getFilePointer();
            String line=readLine(ras);
            this.header=line;
            while(line!=null) {
                indexes.add(position);
                position=ras.getFilePointer();
                line=readLine(ras);
            }
        } 
        return indexes;
    }
    
    /**
     * Lee unha Liña do ficheiro CSV respetando a codificación UTF-8
     * readLine de RandomAccessFile non serve, porque usa 2 bytes por caracter en todos os casos
     * mentres que UTF-8 utiliza 1 byte para representar caracteres en el set ASCII, 
     * dos bytes para caracteres en otros bloques alfabéticos y tres bytes para el resto. Polo tanto
     * si utilizamos letras fora do alfabeto inglés teríamos problemas.
     * readUTF tampouco serve, porque se gardaría o número de bytes do String antes de cada liña.
     */
    private String readLine(RandomAccessFile ras) throws IOException {
        ArrayList<Byte> line=new ArrayList<>();
        try {
            byte cbyte=ras.readByte();
            while(cbyte!='\n') {
                line.add(cbyte);
                cbyte=ras.readByte();
            }
        } catch(EOFException e) {
        }
        int size=line.size();
        if (size==0) return null;
        byte[] chars=new byte[line.size()];
        for(int idx=0;idx<size;idx++) chars[idx]=line.get(idx);
        return new String(chars);
    }
}
