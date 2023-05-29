
package Example;

import Database.Criteria;
import Database.Database;
import Database.DatabaseException;
import Database.IndexedDatabase;
import java.util.Calendar;


public class TestFilm {
    public static void main(String[] args) {
        try {
            Database<Film> fdb=new FilmDatabase("MeusFilmsFavoritos.dat");
            Film f;
            
            System.out.println("Insertando Cinema Paradiso");
            Calendar data=new Calendar.Builder().setDate(1988,11,17).build();
            f=new Film("12345","Nuovo Cinema Paradiso","Giuseppe Tornatore","Italian","Drama",155,data);
            fdb.insert(f);
            try {
                fdb.insert(f);
            } catch(DatabaseException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Insertado Cinema Paradiso !");
            
            System.out.println("\nInsertando Delicatessen");
            data=new Calendar.Builder().setDate(1988,11,17).build();
            f=new Film("8","Delicatessen","Jean-Pierre Jeunet & Marc Caro","French","Fantastic,Drama",95,data);
            fdb.insert(f);
            System.out.println("Insertada Delicatessen !");
            
            // Varío os cast para que se vexan varios modos de facelo.
            System.out.println("\nBuscando Film con isan 12345");
            f=((IndexedDatabase<String,Film>)fdb).get("12345");
            if (f!=null) System.out.println(f);
            else         System.out.println("Non existe ese film");
            
            System.out.println("\nBuscando Film con isan 12");
            f=((FilmDatabase)fdb).get("12");
            if (f!=null) System.out.println(f);
            else         System.out.println("Non existe ese film");
            
            Film[] r;
            
            System.out.println("\nListando os Film de xénero Drama: ");
            // Con clase anónima
            r=fdb.select(new Criteria<Film>() {
                @Override
                public boolean accept(Film r) {
                    return r.getGenre().contains("Drama");
                }
            });
            
            if (r!=null) {
                for(Film film:r) {
                    System.out.println(film);
                }
            } else  System.out.println("Non existen films do xénero Drama");
            
            
            System.out.println("\nListando os Film dirixidos por Giuseppe Tornatore: ");
            // Con expresión lambda
            r=fdb.select((Film r1) -> r1.getDirector().equals("Giuseppe Tornatore"));
            
            if (r!=null) {
                for(Film film:r) {
                    System.out.println(film);
                }
            } else  System.out.println("Non existen films de Giuseppe Tornatore");
            
            
            f=((IndexedDatabase<String,Film>)fdb).get("8");
            if (f!=null) System.out.println(f);
            else         System.out.println("Non existe ese film");
            
            System.out.println("\nEliminando Film con isan 8");
            f=((FilmDatabase)fdb).delete("8");
            System.out.println("Eliminado "+f);
            
            System.out.println("\nBuscando Film con isan 8");
            f=((IndexedDatabase<String,Film>)fdb).get("8");
            if (f!=null) System.out.println(f);
            else         System.out.println("Non existe ese film");
            
            System.out.println("Empaquetando: Eliminando os rexistros marcados como borrados");
            ((IndexedDatabase) fdb).pack();
            
        } catch(DatabaseException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
