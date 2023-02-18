# Enunciado

Se trata de desarrollar una aplicación Java, denominada **PROG07_Tarea** que permita gestionar varios tipos de **cuentas bancarias**. Mediante un menú se podrán elegir determinas operaciones:

1. Abrir una nueva cuenta.
2. Ver un listado de las cuentas disponibles (código de cuenta, titular y saldo actual).
3. Obtener los datos de una cuenta concreta.Realizar un ingreso en una cuenta.
4. Retirar efectivo de una cuenta.
5. Consultar el saldo actual de una cuenta.
6. Salir de la aplicación.

Las cuentas se irán almacenando en alguna estructura en memoria según vayan siendo creadas. Esta estructura podrá contener un máximo de 100 cuentas bancarias. Cada cuenta será un objeto de una clase que contendrá la siguiente información:

* Titular de la cuenta (un objeto de la clase Persona, la cual contendrá información sobre el titular: nombre, apellidos y DNI).
* Saldo actual de la cuenta (número real).
* Número de cuenta (IBAN).
* Tipo de interés anual (si se trata de una cuenta de ahorro).
* Lista de entidades autorizadas para cobrar recibos de la cuenta (si se trata de una cuenta corriente).
* Comisión de mantenimiento (para el caso de una cuenta corriente personal).
* Tipo de interés por descubierto (si es una cuenta corriente de empresa).
* Máximo descubierto permitido (si se trata de una cuenta corriente de empresa)

Las **cuentas bancarias** pueden ser de dos tipos: **cuentas de ahorro** o bien **cuentas corrientes**.

* Las **cuentas de ahorro** son **remuneradas** y tienen un determinado **tipo de interés**.
* Las **cuentas corrientes** no son remuneradas, pero tienen asociada una **lista de entidades autorizadas** para cobrar **recibos domiciliados en la cuenta**.

Dentro de las cuentas corrientes podemos encontrar a su vez otros dos tipos:

* Las cuentas corrientes personales, que tienen una comisión de mantenimiento (una cantidad fija anual).
* Las cuentas corrientes de empresa, que no la tienen. Además, las cuentas de empresa permiten tener una cierta cantidad de descubierto (máximo descubierto permitido) y por tanto un tipo de interés por descubierto y una comisión fija por cada descubierto que se tenga. Es el único tipo de cuenta que permite tener descubiertos.

Cuando se vaya a abrir una nueva cuenta bancaria, el usuario de la aplicación (empleado del banco) tendrá que solicitar al cliente:

* Datos personales: nombre, apellidos y DNI.
* Tipo de cuenta que desea abrir: cuenta de ahorro, cuenta corriente personal o cuenta corriente de empresa.
* Saldo inicial.

Además de esa información, el usuario de la aplicación deberá también incluir:

* Número de cuenta (IBAN) de la nueva cuenta, el cual se validará con una expresión regular y deberá tener formato ESNNNNNNNNNNNNNNNNNNNN, donde N es un dígito del 0 al 9.
* Tipo de interés de remuneración, si se trata de una cuenta de ahorro.
* Comisión de mantenimiento, si es una cuenta corriente personal.
* Máximo descubierto permitido, si se trata de una cuenta corriente de empresa.
* Tipo de interés por descubierto, en el caso de una cuenta corriente de empresa.
* Comisión fija por cada descubierto, también para el caso de una cuenta corriente de empresa.

El programa que escribas debe cumplir al menos los siguientes requisitos:

* Para almacenar los objetos de tipo cuenta deberás utilizar un array.
* Para trabajar con los datos personales, debes utilizar una clase Persona que contenga la información sobre los datos personales básicos del cliente (nombre, apellidos, DNI).
* Para guardar las entidades autorizadas en las cuentas corrientes utiliza una cadena de caracteres.

En cuanto a la organización del código, se deberán crear las siguientes clases:

* Principal: Contendrá el método main y todo el código de interacción con el usuario (lectura de teclado y salida por pantalla).
* Banco: mantendrá como atributo la estructura que almacena las cuentas.Dispondrá de los siguientes métodos:
    * Constructor o constructores.
    * abrirCuenta: recibe por parámetro un objeto CuentaBancaria y lo almacena en la estructura. Devuelve true o false indicando si la operación se realizó con éxito.
    * listadoCuentas: no recibe parámetro y devuelve un array donde cada elemento es una cadena que representa la información de una cuenta.
    * informacionCuenta: recibe un iban por parámetro y devuelve una cadena con la información de la cuenta o null si la cuenta no existe.
    * ingresoCuenta: recibe un iban por parámetro y una cantidad e ingresa la cantidad en la cuenta. Devuelve true o false indicando si la operación se realizó con éxito.
    * retiradaCuenta: recibe un iban por parámetro y una cantidad y trata de retirar la cantidad de la cuenta. Devuelve true o false indicando si la operación se realizó con éxito.
    * obtenerSaldo: Recibe un iban por parámetro y devuelve el saldo de la cuenta si existe. En caso contrario devuelve -1.
* Todas clases e interfaces necesarias para representan la jerarquía de cuentas.
La interfaz Imprimible tan solo declarará el devolverInfoString, que devolverá la información de una cuenta como una cadena de caracteres.
* Otras clases que pudieran ser de utilidad.
