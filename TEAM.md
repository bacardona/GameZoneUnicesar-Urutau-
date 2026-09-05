# TEAM.md — GameZone Unicesar

## Team members

| Full name                     | Student code | GitHub username | Role             |
| Brayan Andres Cardona Reyes   | 1064111056   | bacardona       | Technical Leader |
| Angel David Bermudez Gonzalez | 1067606479   | adbermudez      | Developer 1      |
| Luis Javier Chávez Montes     | 1063788999   | ljchavez1       | Developer 2      |

## Modules and class distribution

### Technical Leader — Sales and Integration Module (Brayan Andres Cardona Reyes)
- `Sale` (domain class)
- `SaleRepository` (persistence)
- `SaleService` (service)
- `ConsoleUI` (user interface)
- `Main` (application entry point)

### Developer 1 — Product Module (Angel David Bermudez Gonzalez)
- `Product` (abstract base class)
- `VideoGame` (subclass)
- `Console` (subclass)
- `ProductRepository` (persistence)
- `ProductService` (service)

### Developer 2 — Person Module (Luis Javier Chávez Montes)
- `Person` (abstract base class)
- `Customer` (subclass)
- `Seller` (subclass)
- `PersonRepository` (persistence)
- `PersonService` (service)

## Feature branches

| Team member      | Branch                   |
| Technical Leader | `feature/sale-module`    |
| Developer 1      | `feature/product-module` |
| Developer 2      | `feature/person-module`  |

## Committed activities

### Technical Leader (Brayan Andres Cardona Reyes)
1. Create the GitHub project repository with initial setup (README, .gitignore, license).
2. Configure the project branches (`main` and `develop`) and enable protection on both.
3. Configure the Maven project with the initial `pom.xml` and the four-layer package structure.
4. Prepare the `TEAM.md` file with team information, assigned roles, and class distribution.
5. Implement the `Sale` domain class with its attributes, constructor, and basic methods.
6. Implement the sale total calculation method (`calculateTotal()`).
7. Implement the `SaleRepository` persistence class.
8. Implement the `SaleService` class with validation rules (minimum one product, stock verification, inventory update).
9. Implement the basic structure of the `ConsoleUI` class (main menu).
10. Implement the submenus of the user interface for each of the three modules.
11. Implement the `Main` class with initial data loading and dependency injection.
12. Review and integrate the developers' pull requests into the integration branch.
13. Prepare the final `README.md` with compilation and execution instructions.

### Developer 1 (Angel David Bermudez Gonzalez)
1. Create the feature branch for the product module.
2. Implement the abstract base class `Product` with its common attributes, constructor, and common methods.
3. Declare the abstract description method that derived classes must implement.
4. Implement the `VideoGame` derived class with its specific attributes and its own implementation of the description method.
5. Implement the `Console` derived class with its specific attributes and its own implementation of the description method.
6. Implement the `ProductRepository` persistence class with save and load methods from files.
7. Implement the `ProductService` class with registration, listing, and stock update methods.
8. Document all classes in the module with JavaDoc in English.
9. Request pull requests to the Technical Leader for module integration.

### Developer 2 (Luis Javier Chávez Montes)
1. Create the feature branch for the person module.
2. Implement the abstract base class `Person` with its common attributes, constructor, and common methods.
3. Declare the abstract or business method that derived classes must implement, according to the analysis.
4. Implement the `Customer` derived class with its specific attributes.
5. Implement the `Seller` derived class with its specific attributes.
6. Implement the `PersonRepository` persistence class with save and load methods from files.
7. Implement the `PersonService` class with registration and listing methods.
8. Document all classes in the module with JavaDoc in English.
9. Request pull requests to the Technical Leader for module integration.