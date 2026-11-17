1. Dibuja la variable original y el objeto creado mediante new Pet("Luna").

Variable                 Objeto

original ──────────────> Pet
                         name = "Luna"

2. ¿Qué valor recibe el parámetro local al iniciar change(original)?
El parámetro local recibe una copia del valor de referencia que contiene original. Por eso, inicialmente, ambas variables apuntan al mismo objeto.

3. ¿Qué objeto modifica local.name = "Sol"?
Modifica el objeto original al que apuntan tanto original como local. Por eso, el atributo name cambia de "Luna" a "Sol" y el cambio es visible desde main().

4. ¿Qué significa local = new Pet("Otra")?
Significa que se crea un nuevo objeto Pet con el nombre "Otra" y se asigna su referencia a la variable local local.

5. ¿Qué imprimirán las dos líneas de salida?
Dentro: Otra
Fuera: Sol

6. ¿Se lanzará el AssertionError?
No. La condición comprueba si original.name es diferente de "Sol".

Variante A — Cambiar el orden. Coloca local = new Pet("Otra");antes de local.name = "Sol";. Predice qué se imprimirá y por qué.

//primero, local deja de apuntar al objeto original y pasa a apuntar a uno nuevo con el nombre "Otra". Después, local.name = "Sol" modifica el objeto nuevo. El objeto original conserva "Luna", por lo que original.name no cambia.

Variante B — Sin reasignación. Eliminar temporalmente local = new Pet("Otra");. Explica por qué ambas impresiones consultan A.

//tanto local como original apuntan al mismo objeto A. Al modificar local.name, también cambia el nombre que consulta original, porque ambas referencias acceden al mismo objeto.

Variante C: Retornar un objeto. Crea un método nuevo que retorna Pet, construye un objeto nuevo y lo devuelve. Asigna el resultado explícitamente en main. Explique por qué el llamador sí puede elegir modificar su propia variable al recibir un valor retornado.

//el método createPet() crea un objeto nuevo y devuelve su referencia. En main, la instrucción original = createPet(); asigna ese valor retornado a la variable original.

Variante D: Agregar un alias. Antes de llamar a change, agrega Pet alias = original;. Después imprime alias.name. ¿Cuántos objetos se crearon al declarar ese alias?

//Se creó cero objetos nuevos al declarar Pet alias = original;. La instrucción solamente copia el valor de referencia de original en alias, por lo que ambas variables apuntan al mismo objeto.

 Explique la diferencia entre reasignar el parámetro y modificar el estado de un objeto .

 //la variable original no queda en null porque Java pasa los argumentos por valor. El parámetro value recibe una copia del valor de referencia de original. Cuando se ejecuta value = null, solamente se modifica el parámetro local; la variable del llamador sigue apuntando al objeto.

2. Depuración — Detecta la afirmación incorrecta
Afirmación incorrecta:
«Como el método modificó el objeto original, Java pasa objetos por referencia».

La corregiría de esta manera:

Java pasa todos los argumentos por valor, incluidos los valores de referencia. El método recibe una copia del valor de referencia y puede utilizarla para modificar el mismo objeto al que apunta la variable del llamador. Sin embargo, el parámetro es una variable local, por lo que su reasignación no cambia la referencia original.

Comprobación — ¿Por qué PetMatch imprime Dentro: Otra y Fuera: Sol?

Paso 1. Antes de llamar al método
Se crea el objeto A con el nombre "Luna".
Variable             Objeto

original ──────────> Objeto A
                      name = "Luna"


Paso 2. El método modifica el objeto original
Cuando se llama a change(original), el parámetro local recibe una copia de la referencia. Ambas variables apuntan al objeto A.

Al ejecutar local.name = "Sol", el nombre del objeto A cambia.
original ──────┐
               ├────> Objeto A
local ─────────┘       name = "Sol"


Paso 3. El parámetro se reasigna
Al ejecutar local = new Pet("Otra"), se crea el objeto B y únicamente local pasa a apuntar a él.
original ───────────> Objeto A
                       name = "Sol"


local ──────────────> Objeto B
                       name = "Otra"


Paso 4. Se imprimen los resultados
Dentro del método, local apunta al objeto B, por eso imprime:
Dentro: Otra


Cuando el método termina, la ejecución regresa a main. La variable original sigue apuntando al objeto A, cuyo nombre es "Sol". Por eso imprime:
Fuera: Sol