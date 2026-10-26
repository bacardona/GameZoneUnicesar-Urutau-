## Exception Handling Analysis — Requirement 6

This document answers the five guiding questions of Requirement 6 (Validations and Exceptions) for GameZone Unicesar.

## Question 1

**The root class GameZoneException can be declared as Exception (checked) or as RuntimeException (unchecked). Which option is more suitable for this project and why? What consequence does it have on the signatures of the service and repository methods?**

**Answer:**

For this project, `GameZoneException` extends `RuntimeException`, which means that it is an unchecked exception. We chose this option because it allows errors to be reported to the menu without forcing every method in the services and repositories to handle them.

For example, if a product does not exist or there is insufficient stock, the system can report the problem and display an appropriate message to the user. This approach also maintains the way the program worked before the changes.

As a result, service and repository methods do not need to declare these exceptions using `throws`. The menu is responsible for catching the exceptions and displaying the corresponding messages.

## Question 2

**Why is it preferable to have a hierarchy of custom exceptions with a common root class (GameZoneException) instead of throwing a generic IllegalArgumentException in every case? Which design principles are applied?**

**Answer:**

Using different exceptions makes it easier to identify the type of problem that occurred. Instead of treating every error in the same way, the system can distinguish between missing resources, business rule violations, invalid data, and problems accessing files.

The main exceptions used in the project are:

- `ResourceNotFoundException`: indicates that a requested resource, such as a customer or product, does not exist.
- `BusinessRuleException`: indicates that a business rule has been violated, such as attempting to make a sale without sufficient stock.
- `InvalidDataException`: indicates that the information entered is incorrect.
- `PersistenceException`: indicates a problem reading or writing the system's data files.

All these exceptions inherit from `GameZoneException`, which allows the system to handle specific errors separately or handle them together when necessary.

This approach follows the Single Responsibility Principle because each part of the system has a specific responsibility. It also supports the Open/Closed Principle because new types of exceptions can be added without having to modify all the existing ones.

## Question 3

**The validators form a new layer (com.gamezone.validation). At which point of the flow are they invoked and why is this location consistent with the layered architecture of the system? Can validators access the persistence package? Justify.**

**Answer:**

Validators are called by the services before an operation is completed or the system's data is modified. Their purpose is to verify that the information is correct and that the operation follows the established rules.

For example, before registering a customer, the system checks that the information is valid and that the customer is not already registered. Before making a sale, it verifies that the required data is correct and that there is sufficient stock.

Validators should not access the persistence package directly because their responsibility is to validate information, not to read or write files. When a validation requires information already stored in the system, the service obtains that information from the repository and provides it to the validator.

This organization maintains the project's layered architecture and makes the code easier to understand, maintain, and test.

## Question 4

**The refactor must preserve the external behavior of the system. Which strategy does the team follow to guarantee that the 10 menu operations keep working the same way? What evidence is shown at the end to demonstrate that nothing was broken?**

**Answer:**

The main strategy is to improve how errors are handled without changing the original business rules. Operations that worked correctly before should continue to work, while incorrect operations should still be rejected, but with clearer and more specific error messages.

The team also organizes the changes into small commits, making it easier to identify and review modifications. In addition, the menu handles the different types of exceptions so that users receive messages appropriate to each problem.

To verify the changes, the project should compile correctly, the original menu operations should continue working, and invalid operations should produce the expected exceptions and messages.

As evidence, the team can present the results of the tests performed before and after the changes. For example, registering a customer with an existing ID should be rejected, and attempting to make a sale without sufficient stock should display an appropriate message.

**Note:** The final report should state that all ten original menu operations were tested only if the team actually performed those tests.

## Question 5

**ConsoleMenu used to catch a single generic exception. Now it must catch four different types. Is this done with a single try block with multiple catch blocks, with cascading catch blocks, or with a general catch (GameZoneException)? Justify the choice considering code clarity and the differentiation of messages to the user.**

**Answer:**

The project uses a single `try` block with several `catch` blocks to handle the different types of exceptions. Each block is responsible for a specific type of error, allowing the system to display a message that explains the problem to the user.

For example, if a requested product does not exist, the system can indicate that the resource was not found. If there is insufficient stock, it can explain that the sale cannot be completed. If a problem occurs while accessing a data file, it can display a more general message.

A final `catch` block for `GameZoneException` can also be used to handle other exceptions belonging to the same hierarchy.

This approach makes the code easier to read and maintain because each type of error has its own handling section. It also improves the user experience by providing clearer messages instead of treating every problem as the same error.
