3. La compatibilidad entre un accesorio y una consola es una relación entre dos
entidades del sistema. ¿Cómo se representa esta relación en el diseño y en la
persistencia? ¿La compatibilidad es un atributo del accesorio, de la consola, o
de ambos?
R) La relación en el diseño y persistencia, se puede dividir en: Diseño, cuando se registra un accesorio, se valida principalmente que dicho accesorio exista en la tienda, hayan al menos 1 (es decir, cantidad suficiente o existente) y finalmente, se valida que sea compatible con una consola. Hablando de persistencia, lo que ocurre aquí, es el almacenamiento de los accesorios ya registrados, asegurando que se guarden en este caso, en un archivo CSV (.txt) con todos los demás, aparte de permitir su respectiva lectura en caso tal, sea solicitado