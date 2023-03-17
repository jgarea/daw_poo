
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Juan
 */
public class Menu {

    private Scanner sc;
    private TreeMap<String, ArtigoBridge> artigo;

    public Menu() {
        sc = new Scanner(System.in);
        artigo = new TreeMap<>();
    }

    public void init() {
        System.out.println("\nEjercicio de Streams");
        System.out.println("--------------------\n");
        int op;
        do {
            impMenu();
            op = Integer.parseInt(sc.nextLine());
            switch (op) {
                case 1:
                    addArtigo();
                    break;
                case 2:
                    print();
                    break;
                case 3:
                    search();
                    break;
                case 4:
                    upperStream();
                    break;
                case 5:
                    upper();
                    break;
                case 6:
                    totalStream();
                    break;
                case 7:
                    total();
                    break;
                case 0:
                    System.out.println("Salir del programa.");
                    break;
                default:
                    System.err.println("Opción incorrecta");
            }
        } while (op != 0);
    }

    private void impMenu() {
        System.out.println("1. Engadir Artigo: solicita artigos");
        System.out.println("2. Listar Artigos: Lista os artigos almacenados ordeados por denominación");
        System.out.println("3. Buscar Artigo: Solicita un código e amosa a informacion do artigo.");
        System.out.println("4. Amosar o máis caro (Stream)");
        System.out.println("5. Amosar o máis caro (iterativo)");
        System.out.println("6. Amosar o importe total (Stream)");
        System.out.println("7. Amosar o importe total (iterativo)");
    }

    private void addArtigo() {
        System.out.println("\nIntroducir datos:");
        System.out.println("Introduce el código:");
        String cod = sc.nextLine();

        System.out.println("Introduce la denominación:");
        String den = sc.nextLine();

        System.out.println("Introduce el precio:");
        double pre = Double.parseDouble(sc.nextLine());

        ArtigoBridge articulo = new ArtigoBridge(new ArtigoFormatter(cod, den, pre));
        if (artigo.put(cod, articulo) != null) {
            System.out.println("Articulo añadido con exito");
        } else {
            System.out.println("El articulo no se ha podido añadir");
        }
    }

    private void print() {
        artigo.values().stream().forEach(x -> System.out.println(x.format(x)));
    }

    private void search() {
        System.out.println("\nIntroducir datos:");
        System.out.println("Introduce el código:");
        String cod = sc.nextLine();
        if (artigo.containsKey(cod)) {
            System.out.println(artigo.get(cod).format(artigo.get(cod)));
        } else {
            System.out.println("Articulo no encontrado");
        }
    }

    private void upper() {
        double max = 0;
        Iterator it = artigo.values().iterator();
        ArtigoBridge ab = null;
        while (it.hasNext()) {
            ab = (ArtigoBridge) it.next();
            if (ab.getPrezo() > max) {
                max = ab.getPrezo();
            }

        }
        System.out.println("El valor máximo es " + max);
    }

    private void upperStream() {
        System.out.println("El valor máximo es " + artigo.values().stream().map((t) -> t.getPrezo()).max((o1, o2) -> 0));
    }

    private void totalStream() {
        double total = artigo.values().stream().map(t -> t.getPrezo()).reduce(0.0, (t, u) -> t + u);
        System.out.println("El valor total es " + total);
    }

    private void total() {
        double total = 0;
        for (ArtigoBridge value : artigo.values()) {
            total += value.getPrezo();
        }
        System.out.println("El valor total es " + total);
    }

}
