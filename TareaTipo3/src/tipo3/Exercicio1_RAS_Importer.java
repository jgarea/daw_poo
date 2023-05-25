package tipo3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.Set;
import java.util.TreeMap;

public class Exercicio1_RAS_Importer implements Importer {
    public void importData() {
        TreeMap<String,Long> index=new TreeMap<>();
        
        try (BufferedReader rdr=new BufferedReader(new FileReader("datos.txt"));
             RandomAccessFile ras=new RandomAccessFile("resultados.txt","rws");
             DataOutputStream out=new DataOutputStream(new FileOutputStream("resultados.idx"))) {
            String line=rdr.readLine();
            // Si fora un CSV, deberíamos saltamos esta liña (a cabeceira) facendo outro rdr.readLine()
            while((line!=null)&&(line.length()>0)) {
                Usuario user=Usuario.fromString(line);
                long position=ras.getFilePointer();
                ras.writeUTF(user.getDni());
                ras.writeUTF(user.getNome());
                ras.writeInt(user.getIdade());
                index.put(user.getDni(), position);
                System.out.println("Importado: "+user);
                line=rdr.readLine();
            }
            Set<String> keys=index.keySet();
            for(String k:keys) {
                out.writeUTF(k);
                out.writeLong(index.get(k));
            }
        } catch(Exception ex) {
            System.out.println("Error: "+ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        Exercicio1_RAS_Importer importer=new Exercicio1_RAS_Importer();
        importer.importData();
    }
}
