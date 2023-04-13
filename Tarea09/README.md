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

# API `java.io`
* `File`: representa un archivo/directorio y permite realizar operaciones como verificar si existe un archivo o directorio, obtener propiedades eliminarlo, crearlo.
* `InputStream` y `OutputStream` clases abstratas para leer y escribir byte a byte,se almacenan en binario. Se utilizan para almacenar un archivo como un ejecutable o una imagen.
    * Clases concretas:
        * ``ByteArrayInputStream``: Crea un InputStream a partir dun array de byte (byte[]) pasado como parámetro
        ao seu construtor. Cando lemos deste Stream lemos do array de datos.
        * ``ByteArrayOutputStream``: Crea un OutputStream de xeito que os bytes que escribimos se almacenan nun
        array de bytes (byte[]). A clase proporciona o método **byte[] toByteArray();** que nos permite recuperar os
        datos escritos.
        
        * `FileInputStream`: Crea un `InputStream` a partir dun obxecto File que se recibe ou crea no construtor da
        clase e que referencia a un ficheiro en disco. Cando lemos deste Stream lemos datos do ficheiro.
        * `FileOutputStream`: Crea un `OutputStream` de xeito que os bytes que escribimos se almacenan nun ficheiro referenciado polo obxecto File que se recibe ou crea no construtor da clase.
        * `BufferedInputStream` , `BufferedOutputStream`
        ---
        * ``DataInputStream``: Permite a lectura de datos primitivos do Stream como char, boolean, byte, float,
        double, ou int.
        * ``DataOutputStream``: Permite volcar datos primitivos como char, boolean, byte, float, double, ou int a un
        fluxo de saída.
        * ``ObjectInputStream``: Permite ler obxectos dun fluxo de bytes escritos con ``ObjectOutputStream``.
        * ``ObjectOutputStream``: Permite volcar obxectos no Stream. Cando se crea o stream sempre se envía unha
        cabeceira mediante o método ``void writeStreamHeader()``, o que debemos ter en conta si queremos engadir
        obxectos ao final de un ficheiro xa existente. Nese caso unha solución e empregar unha clase herdada na
        que este método non faga nada.

* `Reader` y `Writer`: Clases abstractas para leer y escribir caracteres en vez de bytes, se utilizan para almacenar archivos de texto.
    * Clases concretas:
        * `FileReader` , `FileWriter`

        * `BufferedReader` Esta clase proporciona como principal aportación o método ``String readLine()`` que lee
        do fluxo de texto unha liña.

        * `BufferedWriter`  Esta clase realmente non proporciona novas funcionalidades importantes sobre as
        ofrecidas por un Reader simple, pero mellora a súa velocidade facendo uso de un buffer de caracteres. 

        * `PrintWriter` Esta clase proporciona métodos que nos permiten dar formato a conversión en texto da
        información que queremos escribir no Stream, destacando as distintas versións sobrecargadas dos
        métodos **print**, **println** e **printf**

# RandomAccessFile
A clase ``RandomAccessFile`` nos permite crear, almacenar e leer información en arquivos sobre un soporte
almacenamento de acceso aleatorio

**En Java o ficheiro se “abre” cando instanciamos o obxecto RandomAccessFile que o referencia.**

O concepto máis importante dos RandomAccessFile é o “punteiro do ficheiro” ou “posición”. Os bytes
almacenados nun ficheiro están identificados por unha dirección, sendo 0 a dirección do primeiro byte, 1 a do
segundo... etc. 

A clase RandomAccessFile dispón de dous métodos para xestionar o
posicionamento do punteiro do ficheiro:
* ``long getFilePointer();`` que nos devolve o número do byte ao que está apuntando actualmente o punteiro
do ficheiro, e polo tanto o byte que se vai a ler ou escribir na seguinte operación.
* ``void seek(long pos);`` que cambia a posición do punteiro do ficheiro ao byte identificado polo número
indicado.
