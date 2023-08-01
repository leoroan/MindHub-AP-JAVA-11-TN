
// Java - Ejercitación inicial.

import java.util.*;

public class main {

    public static void saludar(String nombre) {
        System.out.println("hola " + nombre + " saludos!!");
    }

    public static String parOimpar(int num) {
        return num % 2 == 0 ? "es par" : "es impar";
    }

    /**
     * Un número se considera primo si es mayor que 1 y no es divisible por ningún
     * otro número excepto 1 y él mismo.
     *
     * @param numero
     * @return boolean
     */
    public static boolean esPrimo(int numero) {
        if (numero <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(numero); i++) {
            if (numero % i == 0) {
                return false; // no es primo
            }
        }
        return true; // es primo
    }

    public static int sumarNumeroImpares(int[] nums) {
        int suma = 0;
        for (Integer num : nums) {
            if (num % 2 != 0) {
                suma += num;
            }
        }
        return suma;
    }

    private static void hacerAlgo(int[] nums) {
        int suma = 0;
        for (Integer num : nums) {
            if (esPrimo(num)) {
                System.out.println("es primo: " + num);
                suma += num;
            }
            if (num % 2 == 0) {
                System.out.println("es par: " + num);
            }
        }
        System.out.println("la suma de los numeros primos es: " + suma);
    }

    private static void mostrarMenu() {
        System.out.println("**** CALCULADORA *****");
        System.out.println("**  1. Sumar        **");
        System.out.println("**  2. Restar       **");
        System.out.println("**  3. Multiplicar  **");
        System.out.println("**  4. Dividir      **");
        System.out.println("**  0. Salir        **");
        System.out.println("*********************");
    }

    private static void calcular(int opcion) {
        Scanner sc = new Scanner(System.in);
        double num1, num2;
        switch (opcion) {
            case 1:
                System.out.print("Ingresa el primer número: ");
                num1 = sc.nextDouble();
                System.out.print("Ingresa el segundo número: ");
                num2 = sc.nextDouble();
                System.out.println("Resultado: " + (num1 + num2));
                break;
            case 2:
                System.out.print("Ingresa el primer número: ");
                num1 = sc.nextDouble();
                System.out.print("Ingresa el segundo número: ");
                num2 = sc.nextDouble();
                System.out.println("Resultado: " + (num1 - num2));
                break;
            case 3:
                System.out.print("Ingresa el primer número: ");
                num1 = sc.nextDouble();
                System.out.print("Ingresa el segundo número: ");
                num2 = sc.nextDouble();
                System.out.println("Resultado: " + (num1 * num2));
                break;
            case 4:
                System.out.print("Ingresa el primer número: ");
                num1 = sc.nextDouble();
                System.out.print("Ingresa el segundo número: ");
                num2 = sc.nextDouble();
                if (num2 != 0) {
                    System.out.println("Resultado: " + (num1 / num2));
                } else {
                    System.out.println("Error, no se puede dividir por 0");
                }
                break;
            case 0:
                System.out.println("Saliendo del menú...");
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
                break;
        }
    }

    public static void calculadora() {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;
        while (opcion != 0) {
            mostrarMenu();
            System.out.println("ingrese un numero de opcion, según la operacion a realizar...");
            opcion = sc.nextInt();
            calcular(opcion);
        }
        sc.close();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Crear las variables correspondientes para leer tu nombre,
        // apellido y edad e imprimirlos por consola en una oración.
        // System.out.println("Ingrese su nombre: ");
        // String miNombre = sc.nextLine();
        // System.out.println("Ingrese su edad: ");
        // int edad = sc.nextInt();
        // System.out.println("Hola " + miNombre + " tu edad es: " + edad);

        // 2. Realizar un pequeño programa que permita ingresar 3 números
        // e imprimir por consola cuál es el mayor.
        // int num1 = -1;
        // for (int i = 0; i < 3; i++) {
        // System.out.println("Ingrese un numero: ");
        // int val = sc.nextInt();
        // if (num1 < val) {
        // num1 = val;
        // }
        // }
        // System.out.println("el numero mas grande es: " + num1);

        // 3. Realizar un pequeño programa que permita el ingreso de un
        // número e imprimir por consola su paridad.
        // System.out.println("Ingrese un numero: ");
        // int val = sc.nextInt();
        // System.out.println(val % 2 == 0 ? "es par" : "es impar");

        // 4. Crear el código correspondiente que permita ingresar 2 cadenas
        // de caracteres e imprima por consola si son iguales o no.
        // String cad1, cad2;
        // System.out.println("Ingrese una cadena de texto / palabra: ");
        // cad1 = sc.nextLine();
        // System.out.println("Ingrese otra cadena de texto / palabra: ");
        // cad2 = sc.nextLine();
        // System.out.println(cad1.equalsIgnoreCase(cad2) ? "son iguales" : "son
        // distintas");

        // 5. Realizar un pequeño programa que permita el ingreso de
        // números, almacenarlos en una colección hasta ingresar un 0.
        // int[] nums = new int[100];
        // List nums = new ArrayList<Integer>();
        // System.out.println("Ingrese un numero: ");
        // int val = sc.nextInt();
        // while (val != 0) {
        // nums.add(val);
        // System.out.println("Ingrese otro numero, o '0' para terminar ");
        // val = sc.nextInt();
        // }
        // System.out.println(nums);

        // 6. Crear una función que imprima un mensaje de bienvenida en la
        // consola.
        // saludar("lean");

        // 7. Crear una función que reciba un número entero y devuelva si es
        // par o impar en forma de texto.
        // System.out.println(parOimpar(11));

        // 8. Crear una función que reciba un número y devuelva si el mismo
        // es primo o no.
        // System.out.println(esPrimo(8) ? "es primo" : "no es primo");

        // 9. Crear una función que reciba una colección de números y
        // devuelva la suma de los números impares.
        // int[] nums = { 2, 4, 6, 9, 1, 99 };
        // System.out.println(sumarNumeroImpares(nums));

        // 10. Crear una función que reciba una colección de números e
        // imprima los números pares y la suma de los números primos.
        // int[] nums = { 2, 4, 6, 9, 1, 99, 7 };
        // hacerAlgo(nums);

        // 11. Crear una función que imprima por consola un pequeño
        // menú con las opciones básicas de una calculadora agregando
        // la opción 0 para salir del mismo.
        // mostrarMenu();

        // 12. Crear otra función que ocupe la del punto anterior para poder
        // crear una pequeña calculadora usando switch. Tener en cuenta
        // el caso de la división por 0.
        // calculadora();

        // 13. Crear el algoritmo necesario para poder gestionar el ingreso
        // a un boliche. El mismo deberá contar con un pequeño menú con
        // las siguientes opciones:
        // ● Ingreso de datos.
        // ● Capacidad disponible.
        // ● Dinero recaudado.
        // ● Salir del sistema.
        // Crear las variables correspondientes para controlar la capacidad
        // y el dinero recaudado. Siendo la capacidad máxima de 500
        // personas y la entrada costar 1500 por persona y 2000 la
        // entrada vip. La primera opción del menú deberá tomar los datos
        // personales del ingresante (nombre, edad, DNI y pase). El
        // algoritmo deberá detectar si la persona está apta para el
        // ingreso, ya que solo se dejará pasar a los mayores de 21 años.
        // Verificar antes de cobrar la entrada si posee pase vip con el cual
        // ingresará gratis o si posee pase con descuento el cual le
        // permitirá solo abonar la mitad del valor de la entrada. Al
        // momento de hacer el cobro se deberá preguntar si quiere
        // comprar la entrada normal o vip y finalmente darle la bienvenida
        // en caso de que todo haya salido bien, volviendo al menú
        // principal. Las variables correspondientes tendrán que reflejar el
        // ingreso tanto de personas como de dinero.
        ingresoAboliche();

    }

    private static final int CAPACIDAD = 500;
    private static final double NORMAL = 1500;
    private static final double VIP = 2000;
    static double dineroRecaudado = 0;
    static int personasTotales = 0;

    public static void mostrarMenuBoliche() {
        System.out.println();
        System.out.println("****        'CHEBOLI'       *****");
        System.out.println("**                             **");
        System.out.println("**  1. Ingreso de datos        **");
        System.out.println("**  2. Capacidad disponible    **");
        System.out.println("**  3. Dinero recaudado        **");
        System.out.println("**  0. Salir del sistema       **");
        System.out.println("**                             **");
        System.out.println("*********************************");
        System.out.println();
    }

    private static void controlar(int opcion) {
        Scanner sc = new Scanner(System.in);
        switch (opcion) {
            case 1:
                System.out.print("Ingresa el nombre: ");
                String nombre = sc.nextLine();
                System.out.print("Ingresa la edad: ");
                int edad = sc.nextInt();
                System.out.print("Ingrese el DNI: ");
                int dni = sc.nextInt();
                boolean pase = false;
                boolean paseConDto = false;
                String opc = "";
                while ((!opc.equalsIgnoreCase("si") && !opc.equalsIgnoreCase("no"))) {
                    System.out.print("Tiene pase VIP? SI o NO: ");
                    sc.nextLine();
                    opc = sc.nextLine();
                    if (opc.equalsIgnoreCase("si")) {
                        pase = true;
                    } else if (opc.equalsIgnoreCase("no")) {
                        pase = false;
                    } else {
                        System.out.println("Ingreso incorrecto...");
                    }
                }

                if (edad >= 21) {
                    if (pase) {
                        System.out.println("Puede pasar");
                        personasTotales++;
                    } else {
                        int opc2 = -1;
                        while (opc2 != 1 && opc2 != 2) {
                            System.out.println("Qué tipo de entrada quiere comprar?");
                            System.out.println("1) - NORMAL");
                            System.out.println("2) - VIP");
                            opc2 = Integer.parseInt(sc.nextLine());
                            if (opc2 == 1) {
                                dineroRecaudado += NORMAL;
                                System.out.println("Persona paga entrada normal");
                                personasTotales++;
                            } else if (opc2 == 2) {
                                dineroRecaudado += VIP;
                                System.out.println("Persona paga entrada VIP");
                                personasTotales++;
                            } else {
                                System.out.println("Opción inválida. Intente nuevamente.");
                            }
                        }
                    }
                } else {
                    System.out.println("no es mayor de edad, NO puede pasar.");
                }
                break;
            case 2:
                int capacidadDisponible = CAPACIDAD - personasTotales;
                System.out.println("Capacidad disponible: " + capacidadDisponible);
                break;
            case 3:
                System.out.println("Dinero recaudado: " + dineroRecaudado);
                break;
            case 0:
                System.out.println("Saliendo del menú...");
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
                break;
        }
    }

    private static void ingresoAboliche() {
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            mostrarMenuBoliche();
            System.out.print("Ingrese una opción: ");
            opcion = sc.nextInt();
            controlar(opcion);
            // System.out.println("Contador de Personas: " + personasTotales +
            // "\nRecaudacion parcial: " + dineroRecaudado);
        } while (opcion != 0);
        sc.close();
        System.out.println("TOTAL Personas: " + personasTotales + "\nRecaudación TOTAL: " + dineroRecaudado);
    }
}
