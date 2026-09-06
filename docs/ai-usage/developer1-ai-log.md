AI Usage Log — Technical Lead
This log records AI-assisted decisions taken by the Developer 1 role during the development of the GameZone Unicesar system.

Entry 1

Date: 2026-09-05, 8:03 PM
Tool used: Claude (web chat)

Reason for use: Create the save method to save the list of products in a text file.

Problem faced: I needed to make a method that could save the products from the list into a file, but I was not sure how to organize the information depending on whether the product was a video game or a console.

Prompt used: "Necesito hacer un metodo llamado save que reciba una lista de productos y los guarde en un archivo de texto. Quiero que recorra todos los productos de la lista y, dependiendo de que tipo de producto sea, guarde sus datos separados por ;. Si el producto es un VideoGame, que empiece la linea con VG y guarde: id, titulo, precio, cantidad, plataforma, genero y clasificacion de edad. Si el producto es una Console, que empiece con CO y guarde: id, titulo, precio, cantidad, marca, modelo y generacion. Cada producto debe quedar en una linea diferente del archivo. Tambien quiero que se maneje el error si ocurre algun problema al guardar el archivo."

Solution obtained and decision taken: The solution was to use a BufferedWriter and FileWriter to write the products to the file. The method checks if each product is a VideoGame or a Console using instanceof, creates a line with all its information separated by ;, and then writes each product on a new line.

Entry 2

Date: 2026-09-05, 8:34 PM
Tool used: Claude (web chat)

Reason for use: Create the load method to read the products that were previously saved in the data file.

Problem faced: I needed to make a method that could read the information from the file and turn each line back into a product, but I was not sure how to separate the data and create the correct type of product.

Prompt used: "Necesito hacer un metodo llamado load que lea los productos que estan guardados en un archivo de texto y los devuelva en una lista. Si el archivo no existe, debe devolver una lista vacia. Quiero que lea cada linea, separe los datos usando ; y use el primer dato para saber si es un VideoGame o una Console. Despues debe tomar el resto de los datos como id, titulo, precio, cantidad y los demas atributos, y crear el producto correspondiente. Tambien quiero que maneje los errores si ocurre algun problema al leer el archivo."

Solution obtained and decision taken: The solution was to use BufferedReader and FileReader to read the file line by line. The method checks if the file exists, separates each line using ;, converts the price and quantity to the correct data types, and uses instanceof-style type checking with the VG and CO identifiers to create either a VideoGame or a Console. If there is an error while reading, it displays an error message and returns the products that were loaded.

Entry 3

Date: 2026-09-05, 9:15 PM
Tool used: Claude (web chat)

Reason for use: Create the updateStock method to update the quantity of a product after a sale.

Problem faced: I needed to make a method that could reduce the stock when a product was sold, but I also needed to make sure that the quantity sold was not greater than the available stock and that the product actually existed.

Prompt used: "Necesito hacer un metodo llamado updateStock que reciba el id de un producto y la cantidad que se vendio. Quiero que busque el producto por su id, revise si hay suficiente stock antes de descontar la cantidad vendida y, si hay suficiente, actualice la cantidad del producto. Despues debe guardar los cambios usando productRepository.save(products). Si no hay suficiente stock, debe mostrar un mensaje y no hacer el cambio. Si el producto no existe, tambien debe mostrar un mensaje."

Solution obtained and decision taken: The solution was to use a for loop to search for the product by its ID. If it is found, the method checks whether there is enough stock. If there is, it subtracts the quantity sold from the current stock and saves the updated list. If there is not enough stock or the product does not exist, it displays an appropriate message.