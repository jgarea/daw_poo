# Enunciado
Estamos en disposición de dar persistencia a los datos que utilizan nuestras aplicaciones. Hasta el momento los datos manejados solo se mantienen en memoria principal: cuando nuestras aplicaciones finalizan la ejecución todos los datos se pierden.

## Ejercicio 1

Se trata de modificar la aplicación desarrollado en la Unidad de Trabajo 8, Ejercicio 1 para dar persistencia a los datos de cuentas bancarias. El nombre será **PROG09_Ejerc1** Para ello:

* Cuando la aplicación finalice, es decir, el usuario seleccione la opción Salir, la aplicación volcará el contenido de la estructura de datos con las cuentas bancarias a un fichero binario denominado ``datoscuentasbancarias.dat``.
* Cuando la aplicación inicie la ejecución, antes de mostrar el menú, deberá cargar en la estructura de datos el contenido del fichero ``datoscuentasbancarias.dat``.

Como ya sabes, para poder realizar estas tareas es necesarios que nuestros objetos que representan cuentas bancarias sean serializables. Habrá que realizar las convenientes modificaciones a la clase ``CuentaBancaria``.

## Ejercicio 2

Añade una nueva opción al menú de la aplicación denominado "*Listado clientes*" de modo que al seleccionarla, se genere un fichero de texto denominado L``istadoClientesCCC.txt`` que contenga una línea de texto por cada cuenta bancaria almacenada, donde se visualice nombre del propietario y CCC por cada una de ellas.

La última línea del fichero contendrá el número total de cuentas existente.

## Codigo de ficheros:
```java
// asignamos el nombre del fichero si no está creado lo crea con la función privada createFile();
File file = new File("datoscuentasbancarias.dat");
if(!createFile(file))
    loadData(file); // Función que lee los datos del fichero
```
```java
/**
 * Crea el fichero si no existe
 * @param file fichero a crear
 * @return boolean si se ha creado el fichero
 */
private boolean createFile(File file) {
    boolean fichero = false;
    if(!file.exists()){
        try {
            fichero = file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    return fichero;
}
```
```java
/**
 * Lee los datos del fichero
 * @param file fichero a utilizar
 */
private void loadData(File file) {
    try (InputStream inputStream= new FileInputStream(file);
         ObjectInputStream objectInput = new ObjectInputStream(inputStream)){
        
        banco=(Banco)objectInput.readObject();
        
    } catch (FileNotFoundException ex) {
        throw new RuntimeException(ex);
    } catch (IOException ex) {
        throw new RuntimeException(ex);
    } catch (ClassNotFoundException ex) {
        throw new RuntimeException(ex);
    }
}
```
```java
/**
 * Guarda los datos del banco en el fichero
 * @param file 
 */
private void saveData(File file) {
    try (OutputStream outputStream = new FileOutputStream(file);
         ObjectOutputStream objectOuput = new ObjectOutputStream(outputStream)){
        objectOuput.writeObject(banco);
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```
```java
/**
 * Crea un fichero donde almacena los datos de los clientes 
 * y el numero de cuentas existentes
 */
private void listClient() {
    
    File file= new File("ListadoClientesCCC.txt ");
    createFile(file);
    
    try(Writer writer = new FileWriter(file)) {
        String[] clientes=banco.getClientesCCC();
        for (int i = 0; i < clientes.length; i++) {
            writer.write(clientes[i]+"\n");
        }
        writer.write("Numero de cuentas existentes: "+clientes.length);
        System.out.println("\nExportando a ListadoClientesCCC.txt\n");
    } catch (IOException ex) {
        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
    }
    
}
```