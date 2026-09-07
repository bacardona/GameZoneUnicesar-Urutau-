1. ¿Los accesorios deben integrarse a la jerarquía existente de productos (extendiendo Product) o deben conformar una jerarquía independiente? Justifique su decisión considerando la reutilización de código y la coherencia del modelo. 

R/Los accesorios deben integrarse ya que comparten atributos comunes con los demás productos de la tienda heredandolos y usándolo en su misma clase

2. ¿Qué atributos son comunes a los tres tipos de accesorios y cuáles son específicos de cada tipo? ¿Cómo se refleja esta distinción en la jerarquía de clases del módulo? 

Los atributos comunes son:identificador, título, precio y cantidad disponible en inventario. Los específicos:
Controles: tipo de conexión (inalámbrico o alámbrico) y por la lista de consolas con las que son compatibles
Usb:capacidad de almacenamiento en gigabytes y por su tipo (SD, microSD, tarjeta interna). 
Cables: longitud en metros y por el tipo de conector (HDMI, USB, óptico, entre otros)

se refleja de manera que estos tipos se guardan de manera individual en la clase accesory
