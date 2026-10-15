public class ErrorComparaciones {
    public static void main(String[] args) {
        int edad = 20;
        boolean activo = true;

        boolean mayorEdad = edad == 18;
        boolean habilitado = edad >= 18 && activo;

        System.out.println(mayorEdad);
        System.out.println(habilitado);

    }
}

// qué operador debería utilizarse para comparar igualdad; deberia utilizarse el operador == para comparar igualdad en Java.
// qué operador lógico utilizaremos para expresar “y”; deberia utilizarse el operador && para expresar “y” en Java.

/* 
qué diferencia existe entre = y ==; el operador = se utiliza para asignar un valor a una variable, mientras que el operador == se utiliza para comparar si dos valores son iguales.
qué resultado produce una comparación; una comparación produce un valor booleano (true o false) dependiendo de si la condición evaluada es verdadera o falsa.
cuándo usar &&; debería utilizarse el operador && cuando se desea evaluar si dos condiciones son verdaderas al mismo tiempo.
cuándo usar ||; cuándo debería utilizarse el operador || cuando se desea evaluar si al menos una de las condiciones es verdadera.
cómo comparar el contenido de dos String. cómo comparar el contenido de dos String; para comparar el contenido de dos String en Java, se debe utilizar el método equals() en lugar del operador ==, ya que este último compara referencias de objetos y no su contenido.
 */