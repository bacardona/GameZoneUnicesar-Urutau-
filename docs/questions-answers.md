R1 — Accessories

Question 3

The compatibility between an accessory and a console is a relationship between two entities of the system. How is this relationship represented in the design and persistence? Is compatibility an attribute of the accessory, the console, or both?

Answer:

The compatibility between an accessory and a console is represented as a relationship between the two entities. The accessory can store a list of compatible console identifiers, which allows the system to know which consoles can be used with that accessory. In persistence, this relationship can be stored in the accessory record in the CSV file, using the console identifiers as part of the saved data.

The compatibility should be considered an attribute of the accessory because the accessory is the entity that needs to know which consoles it supports. This avoids duplicating the same compatibility information in the console and keeps the relationship easier to manage.

Question 4

What modifications are necessary in the sales service class (SaleService) so that sales can include accessories without breaking the existing behavior with video games and consoles?

Answer:

The SaleService must be modified so that accessories can be included in a sale without changing the existing behavior for video games and consoles. The service should continue using the product IDs to find the products, validate their stock, calculate the requested quantities, and update the stock.

Since accessories are also products, they can be handled through the existing Product abstraction and the same sale process. The important modification is to make sure that the service accepts accessories as valid products and applies the corresponding stock update without creating a separate sale process. This keeps the existing behavior intact while extending the system to support the new product type.

R2 — Promotions

Question 3

The business rule establishes that only the promotion with the highest discount is applied. In which class should this selection logic be located and why is this location consistent with the layered architecture principle? Why should this logic NOT be in the Sale class or in the console menu?

Answer:

The rule that only the promotion with the highest discount should be applied belongs in PromotionService, because this class is responsible for the business logic related to promotions. Keeping this decision in the service layer follows the architecture of the system, where business rules are handled by services rather than by the model or the user interface.

This logic should not be placed in the Sale class because Sale represents the sale itself and should not be responsible for searching and selecting promotions. It should also not be placed in the console menu because the menu is part of the user interface and should only handle interaction with the user. Keeping the logic in PromotionService makes the code easier to maintain and allows the same rule to be reused from different parts of the system.

Question 4

What modifications are necessary in the Sale class and in the generateReceipt method so that the receipt displays the applied discount? Do these modifications break any existing behavior in the system?

Answer:

The Sale class must be modified so that it can store and expose the discount applied to the sale and calculate the final amount after the discount. The generateReceipt method must then include information about the selected promotion, the discount amount, and the final total paid by the customer.

These changes should be additive, meaning that the existing behavior of the sale should continue working when no promotion is applied. If there is no applicable promotion, the discount can remain zero and the final total can remain equal to the original sale total. Therefore, the modification extends the existing functionality without breaking the previous behavior of the system.

R3 — Returns

Question 3

The business rule establishes that returns can only be registered within 30 days after the sale. In which layer of the system should this validation be located and why? What Java mechanism is used to calculate the difference between two dates?

Answer:

The validation that a return can only be registered within 30 days after the sale belongs in the ReturnService, because this is a business rule of the return process. The service layer is responsible for validating these rules before creating and saving a return.

To calculate the difference between the sale date and the current date, Java's ChronoUnit.DAYS can be used together with LocalDate. This allows the system to calculate the number of calendar days that have passed and verify whether the return is still within the 30-day limit.

Question 4

The return of products increases the stock. What existing method from the Workshop 1 system is reused for this operation, and in which class is it invoked from the returns module? Why is it important to reuse existing methods instead of duplicating the stock update logic?

Answer:

The return process should reuse the restoreStock method in ProductService. This method increases the stock of a product after a successful return, and it is invoked from ReturnService when the returned products have been validated.

Reusing an existing method is important because the stock update logic is already centralized in ProductService. This avoids duplicating the same logic in ReturnService, reduces the possibility of inconsistencies or errors, and keeps the responsibilities of the different layers clear.

R4 — Warranties

Question 3

The duration of each warranty type is different (6 months or 12 months). How is the expiration date calculated in each subclass? Should this calculation be done in the warranty constructor or in a separate method? Justify your answer.

Answer:

The expiration date is calculated according to the type of warranty. BasicWarranty has a duration of 6 months, while ExtendedWarranty has a duration of 12 months. Each subclass implements the getDurationInMonths() method with its corresponding value.

The calculation is done in the Warranty constructor. After receiving the start date, the constructor calculates the end date by adding the number of months returned by getDurationInMonths(). This avoids duplicating the same date calculation in both subclasses. The subclasses only define their specific duration, while the common calculation remains in the abstract Warranty class.

Question 4

The extended warranty adds a cost of 10% of the product price to the sale total. At what point in the sales registration flow is this additional cost calculated and applied? What modifications are necessary in the SaleService.registerSale method?

Answer:

The additional cost of the extended warranty should be calculated when the sale is being registered, after the sale and the products have been validated. When a console has an extended warranty requested, SaleService.registerSale should call WarrantyService.assignExtendedWarranty(...) and add the cost returned by getAdditionalCost() to the sale total.

The registerSale method must therefore receive additional information indicating which products have an extended warranty, for example a List<String> containing the product IDs. It should then generate the basic warranty automatically for consoles and generate an extended warranty only for the consoles selected by the user. The cost of each extended warranty is 10% of its product price and must be added to the final sale total.

