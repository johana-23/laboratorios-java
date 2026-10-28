Comprobación
Debes poder dibujar la secuencia de invocaciones main→ calculate→ doubleValue, reconocer las variables locales de cada método y justificar por qué un método no puede continuar en una instrucción que depende de un retorno todavía pendiente.


  
                
                 main()
                   |
                   | initialValue = 4
                   |
                   v
          calculate(initialValue)
                   |
                   | value = 4
                   |
                   v
           doubleValue(value)
                   |
                   | number = 4
                   |
                   | return number * 2
                   | return 4 * 2 = 8
                   |
                   v
          calculate() recibe 8
                   |
                   | doubled = 8
                   | return doubled + 3
                   | return 8 + 3 = 11
                   |
                   v
              main() recibe 11
                   |
                   | result = 11
                   |
                   v
         Imprime: Final result: 11


Variables locales de cada método
Cada método tiene sus propias variables locales. Aunque algunas tengan valores relacionados, no son la misma variable.
Método	Variable local	Valor	Función
main()	args	—	Recibe los argumentos del programa.
main()	initialValue	4	Guarda el valor inicial.
main()	result	11	Guarda el resultado devuelto por calculate().
calculate()	value	4	Recibe el valor enviado por main().
calculate()	doubled	8	Guarda el resultado de doubleValue().
doubleValue()	number	4	Recibe el valor enviado por calculate().

La razón es que una instrucción que necesita el resultado de una llamada no puede completarse hasta que esa llamada devuelva un valor. Mientras doubleValue() se está ejecutando, calculate() queda suspendido temporalmente en la línea que lo invocó. Cuando recibe el retorno, continúa desde ese pun