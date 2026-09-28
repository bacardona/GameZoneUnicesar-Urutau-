Requirement 1 — Accessories

Question 1: Should accessories be integrated into the existing product hierarchy (extending Product) or form an independent hierarchy? Justify your decision considering code reuse and model coherence.

Answer: Accessories should extend Product. An accessory is sold just like a video game or a console: it has an id, a title, a price, and a stock quantity. If we put accessories inside the Product hierarchy, we reuse those four attributes and their getters and setters instead of copying them into three new classes. Also, Sale and SaleService already work with Product, so they can sell accessories with almost no changes. With a separate hierarchy we would duplicate code and have to handle two kinds of "sellable things" all over the system, which would make it more confusing.

Question 2: Which attributes are common to the three accessory types and which are specific to each type? How is this distinction reflected in the module's class hierarchy?

Answer: The common attributes are id, title, price, and quantity (which come from Product), plus the list of compatible consoles (which lives in Accessory). The specific attributes are:Controller, Cable, Memory

In the hierarchy, Accessory is an abstract class that holds what is common, and Controller, Cable, and Memory inherit from it and add only what makes them different.

Requirement 2 — Promotions

Question 1: The three promotions have different calculation rules but share common attributes and behaviors. How is this situation reflected in the class hierarchy design? Which object-oriented programming mechanism allows each promotion type to calculate its discount differently without the rest of the system needing to know the concrete types?

Answer: We create an abstract parent class Promotion with what every promotion shares: id, name, start date, end date, and the isActive method. PercentageDiscount, CategoryDiscount, and BulkPurchaseDiscount inherit from it, each with its own data (percentage, category, minimum quantity). The mechanism that lets each one calculate differently is polymorphism. The rest of the system just calls promotion.calculateDiscount(sale) without knowing which type it is, and Java automatically runs the version that belongs to that object.

Question 2: The base class Promotion cannot implement the discount calculation method because each type has different logic. How is this method declared in the base class and what does this declaration guarantee for the subclasses?

Answer: It is declared as an abstract method with no body: public abstract double calculateDiscount(Sale sale);. This guarantees that every concrete subclass must implement it, because otherwise the code does not compile. So any Promotion object always has that method, even though each one does it in its own way. Also, since the class is abstract, we cannot create a Promotion directly, only its child classes.

Requirement 3 — Returns

Question 1: A return is a new system entity that references an existing sale. What type of relationship exists between the Return class and the Sale class? Is it inheritance, association, aggregation, or composition? Justify.

Answer: It is an association. A return keeps a reference to the original sale, but a return is not a kind of sale (so it is not inheritance). The sale also does not "contain" or "own" the return: the sale exists on its own, it was created earlier, and it keeps existing even if there are never any returns (so it is not composition or aggregation). Return just needs to know which sale it belongs to in order to validate and show the data.

Question 2: A return may contain only some of the products from the original sale, not necessarily all of them. How is this situation represented in the attributes of the Return class? What is stored in the returned products attribute?

Answer: The Return class has two separate attributes: originalSale, which is the whole sale, and returnedProducts, which is a list of Product. That list only stores the products the customer actually returned, which can be a few or all of the ones in the sale. This way, the sale keeps what was bought and the return keeps only what was sent back. The refund is calculated by adding up the prices in returnedProducts, not the prices of the whole sale.

Requirement 4 — Warranties

Question 1: The two warranty types share common attributes (dates, associated product) but also have different attributes and behaviors (duration, coverage, cost). How is this situation reflected in the class hierarchy design? Which object-oriented programming mechanism allows each warranty type to have its own duration without duplicating code?

Answer: We make an abstract parent class Warranty with what is common: id, product, sale, start date, end date, isActive, and the certificate. BasicWarranty and ExtendedWarranty inherit from it. What changes between them (duration, type name, and additional cost) is declared in Warranty as abstract methods, and each child implements them its own way: the basic one lasts 6 months and costs nothing, and the extended one lasts 12 months and costs 10% of the product price. What makes this possible without repeating code is inheritance together with polymorphism (overriding abstract methods). The Warranty constructor calculates the end date only once using getDurationInMonths(), and it works for both children.

Question 2: The business rule states that only consoles generate an automatic basic warranty, not video games. In which layer of the system is this decision located and which Java mechanism is used to check the real type of a product? Justify.

Answer: The decision belongs in the service layer, specifically in SaleService.registerSale, because it is a business rule and that layer is the one that coordinates the rules when registering a sale. The model only stores data and the UI only shows and asks for information, so neither of them should decide this. To check the real type we use the instanceof operator: even if the list has objects declared as Product, product instanceof Console tells us whether it is actually a console or a video game.