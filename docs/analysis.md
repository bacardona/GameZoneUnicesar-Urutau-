# Analysis and Design — GameZone Unicesar

## People in the system

### 1. What attributes are common to all people who interact with the store, and which are specific to each type of person? How is this distinction reflected in a class hierarchy?

The common attributes are name, identification, and phone number. Customers, in addition to these attributes, have an email address and a purchase history, while sellers have an employee code and an assigned work shift.

In the class hierarchy, a general class called `Person` can be created, containing the common attributes: name, identification, and phone number. From this class, the `Customer` and `Seller` classes can be created, which inherit these attributes and add their own specific characteristics. In this way, `Person` would be the parent class, while `Customer` and `Seller` would be the child classes.

### 2. Should there be a class that represents a "generic person" without specifying a role? Why or why not? What implication does this decision have on the possibility of instantiating that class?

Yes, a class representing a generic person should exist, since it allows grouping the attributes that are common to customers and sellers, such as name, identification, and phone number. However, this class should be abstract, because a person within the context of the store must fulfill a specific role, such as customer or seller. By being abstract, objects cannot be created directly from the `Person` class; instead, it is used as a base for the `Customer` and `Seller` classes.

## Products in the system

### 3. What characteristics do all products sold by the store have in common, regardless of type? What characteristics are specific to each type of product?

All products share characteristics such as price, title, identifier, and available quantity in the store.

The specific characteristics depend on the type of product. For consoles, these are brand, model, and generation. For video games, they include platform, genre, and recommended age rating.

### 4. Each type of product must be able to present a description that integrates its particular characteristics. How should this behavior be declared in the base class to guarantee that all subclasses implement it in their own way? What object-oriented programming mechanism allows this?

An abstract method for the description should be declared in the base class `Product`. Each subclass must then implement this method according to its own characteristics. This is achieved through inheritance and polymorphism. The keyword `extends` is used to indicate that a class inherits from another.

## Sales and relationships between entities

### 5. A sale involves a customer, a seller, and one or more products. What type of relationships exist between the class that represents the sale and the other classes in the system? Are these relationships inheritance, association, composition, or another type? Justify.

These relationships are association, not inheritance or composition. It is not inheritance because `Customer`, `Seller`, and `Product` are not specializations of `Sale`. It is not composition either, because customers, sellers, and products all exist independently in the system (they continue to exist even if a sale is deleted).

It is also worth clarifying two nuances within this association: the relationship of `Sale` with `Customer` and with `Seller` is a simple association (a sale "knows about" a specific customer and a specific seller).

The relationship with `Product`, on the other hand, is an association of the aggregation type, because a sale groups together a collection of products (a minimum of one, `1..*`) that exist completely independently in the inventory — the product does not "belong" to the sale, it simply participates in it.

### 6. Should the sale be responsible for calculating its own total, or should this responsibility belong to another class? Argue your decision.

Yes, it should be responsible for calculating its own total, since — in order to avoid redundancy — the class has access to the information necessary for the calculation (such as the products, their prices, and quantities), which allows the sale total to be calculated by the class itself.

## Business rules

### 7. How is it guaranteed in the design that a sale cannot be registered without at least one product? At what point in the system should this rule be validated?

When registering a sale, it must be validated that it contains at least one product and that said product exists in the system. This validation should be performed in the Service layer, before the sale is sent to the repository to be stored in the system.

### 8. How is the automatic inventory update reflected in the design when a sale is registered? Which classes are involved in this operation?

The classes involved in this operation are: `Sale` (which contains the list of products sold and their quantities), `Product` (whose stock attribute is modified), `SaleService` (responsible for coordinating the operation), and `ProductService`/`ProductRepository` (which execute and persist the inventory change).

## Layer organization

### 9. The system must be organized into four layers: model, persistence, services, and user interface. What type of classes belong to each layer? What criteria determine which layer a class should be placed in?

In the model layer, we find the main classes: `Person`, `Customer`, `Seller`, `Product`, `VideoGame`, `Console`, and `Sale`. In the persistence layer, we have `PersonRepository`, `SaleRepository`, and `ProductRepository`. In the service layer, we have `PersonService`, `SaleService`, and `ProductService`, and finally, in the UI layer, we have `Main` and `ConsoleUI`.

The criteria that determine this are mainly:
- A class must be placed in the layer that corresponds to the responsibility it fulfills within the system.
- If it represents an entity, it belongs to the Model.
- If it stores or retrieves information, it belongs to Persistence.
- If it executes operations or business rules, it belongs to Services.
- If it interacts with the user, it belongs to the User Interface.

### 10. Why should the logic for saving and retrieving data from files not be inside the domain classes? What problems arise when these responsibilities are mixed?

Because each class must have a specific responsibility, as explicitly explained: the domain classes handle business logic, while the repositories are responsible for saving and retrieving data.

When these responsibilities are mixed, the system becomes harder to maintain, and errors can occur in the handling of information, such as duplication or loss of important data.

### 11. What dependencies are allowed between layers, and which are forbidden? Justify the purpose of the allowed dependencies.

**Allowed dependencies:**
- **UI → Services:** because the interface receives the user's actions and requests them from the services. For example, the user selects "Register sale" and `ConsoleUI` calls `SaleService`.
- **Services → Persistence:** since the services need the repositories to save or retrieve information. For example, `SaleService` uses `SaleRepository` to store a sale.
- **Services → Model:** since the services need to work with the system's entities, such as `Sale`, `Product`, or `Customer`.
- **Persistence → Model:** the repositories need to know the entities they are going to save or retrieve.

**Forbidden dependencies:**
- **UI → Persistence:** the interface must never access the repositories directly; every operation must first pass through the Service layer.
- **Model → any other layer:** the Model must not depend on Persistence, Services, or the UI, since the domain entities (`Person`, `Product`, `Sale`, etc.) must be independent of how they are stored or displayed — this allows them to be reused without dragging in file-handling or console logic.

These restrictions guarantee that changes in one layer (for example, switching from text files to a database) do not force changes in the other layers.