Dibuja tres objetos identificados como A, B y C. Incluye pet, alias, currenty replacementen el momento en que esas variables existen.

¿pet y alias apuntan inicialmente al mismo objeto?
Sí. La instrucción PetProfile alias = pet; copia el valor de referencia de pet. No crea otro objeto.

Diagrama inicial:
VARIABLES              OBJETOS

pet ──────────┐
              ├──────> OBJETO A
alias ────────┘        name = "Luna"
                       visits = 0