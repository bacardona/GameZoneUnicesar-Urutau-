# GameZone Unicesar

**Workshop 2 — Programming III — Systems Engineering — Universidad Popular del Cesar**

## Overview

GameZone Unicesar is a simulated retail platform for video games and consoles, set in Valledupar's university district. The application manages three core entities — products (games and consoles), individuals (buyers and staff), and transactions — while keeping stock levels synchronized automatically each time a sale goes through.

It's implemented as a console-based Java program following a rigid four-tier design (model, persistence, service, ui). Persistence is handled through flat files rather than a database, so records survive between sessions without any external dependency.

## Team

The project was built by a group of three, with each member owning a full vertical slice (all four layers) of one module:

| Member | Responsibility |
|---|---|
| Technical Lead | Sales & System Integration |
| Developer 1 | Product Catalog |
| Developer 2 | People Management |

Full breakdown of responsibilities, class ownership, and individual contributions is available in [TEAM.md](TEAM.md).

## Architecture

All source code lives under the `com.gamezone` package, split into four layers with a one-way dependency chain — nothing may depend "upward":

- `model` sits at the base and depends on nothing.
- `persistence` depends only on `model`.
- `service` depends on both `model` and `persistence`.
- `ui` sits at the top and depends only on `service`.

```
┌─────────────────────────────┐
│             ui              │   console menu
└───────────────┬─────────────┘
                │  calls
                ▼
┌─────────────────────────────┐
│           service           │   business rules
└───────┬───────────────┬─────┘
        │               │
        ▼               ▼
┌───────────────┐   ┌─────────────┐
│  persistence  │   │    model    │
│  (file I/O)   │─▶│  (domain)    │
└───────────────┘   └─────────────┘
```

For the complete rationale behind the layering, see [docs/layers-diagram.md](docs/layers-diagram.md). The inheritance tree is in [docs/hierarchy-diagram.md](docs/hierarchy-diagram.md) and the full class-level design is in [docs/class-diagram.md](docs/class-diagram.md).

## Accessories Module (Requirement 1)

### Model

`Accessory` is an abstract class that extends `Product`, so accessories share the base product data (id, name, price, stock). It has three concrete types:

- `Controller`
- `Cable`
- `Memory`

Each accessory can be checked for compatibility with a given console.

### Services and persistence

- `AccessoryRepository` stores accessories in `data/accessories.csv`.
- `AccessoryService` holds the business rules for registering, listing, and querying accessories, and for updating accessory stock.

### Sales with accessories

`SaleService.registerSale` accepts products and accessories in the same sale, with unified stock validation and a single total. When a sale is completed, the inventory update is delegated to `ProductService` or `AccessoryService` depending on the type of each item. Item IDs must be unique across video games, consoles, and accessories, so a sale line always resolves to exactly one item.

### Accessory management menu

1. Register a controller.
2. Register a cable.
3. Register a memory card.
4. List all accessories.
5. List accessories by type.
6. Show accessories compatible with a given console.

When recording a sale, the user can now pick accessories in addition to products.

## Promotions Module (Requirement 2)

### Model

`Promotion` is an abstract class with an id, a name, and a validity period (start and end date). It has three concrete types:

- `PercentageDiscount`: a percentage off the sale subtotal.
- `CategoryDiscount`: a percentage off the items of a given category (video games, consoles, or accessories).
- `BulkPurchaseDiscount`: a percentage applied when the sale reaches a minimum quantity of products.

### Services and persistence

- `PromotionRepository` stores promotions in `data/promotions.csv`.
- `PromotionService` registers and lists promotions, and selects the best one for a sale through `findBestPromotionFor`.

### Promotions in sales

`SaleService.registerSale` asks `PromotionService` for the best active promotion (the one with the highest discount) and applies it over the subtotal. The discount can never exceed the subtotal. `Sale` stores the name of the applied promotion and the discount amount, and `generateReceipt` shows the subtotal, the discount with the promotion's name, and the final total.

### Promotion management menu

1. Register a percentage discount.
2. Register a category discount (video games, consoles, or accessories).
3. Register a bulk purchase discount.
4. List all promotions.
5. List active promotions.

### Documentation

- [Promotion analysis](docs/promotion-analysis.md)
- [Promotion class diagram](docs/promotion-class-diagram.md)

## Prerequisites

- JDK 17 or newer
- Apache Maven (auto-resolved by NetBeans)
- Apache NetBeans IDE

## Running the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/bacardona/GameZoneUnicesar-Urutau-.git
   ```
2. In NetBeans, go to **File → Open Project** and select the cloned directory — NetBeans will pick up the `pom.xml` automatically.
3. Give NetBeans a moment to fetch the Maven dependencies.
4. Right-click the project and choose **Run** (or hit `F6`). This compiles the project and launches `Main`, which opens the console menu.

Command-line alternative:

```bash
mvn clean compile
mvn exec:java "-Dexec.mainClass=com.gamezone.Main"
```

## Seed Data

The [`data/`](./data) directory ships pre-populated with at least three sellers, since staff are treated as already on payroll and are never created through the UI. It also includes `accessories.csv` with sample accessories and `promotions.csv` with three preloaded promotions. Every record — products, accessories, promotions, people, and sales — is written to disk after each operation and read back in automatically the next time the app starts.

## Menu Features

**Product catalog**

1. Add a new video game.
2. Add a new console.
3. Browse the full inventory.

**People management**

4. Register a new customer.
5. Browse registered customers.
6. Browse registered sellers.

**Sales**

7. Record a new sale (customer, seller, and one or more products or accessories).
8. Review the entire sales log.
9. Look up a given customer's purchase history.
10. Look up the sales handled by a given seller.

## Repository Layout

```
GameZoneUnicesar/
├── README.md
├── TEAM.md
├── pom.xml
├── .gitignore
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── gamezone/
│                   ├── model/
│                   ├── persistence/
│                   ├── service/
│                   ├── ui/
│                   └── Main.java
├── data/
└── docs/
    ├── analysis.md
    ├── accessory-analysis.md
    ├── promotion-analysis.md
    ├── hierarchy-diagram.md
    ├── class-diagram.md
    ├── accessory-class-diagram.md
    ├── promotion-class-diagram.md
    ├── layers-diagram.md
    └── ai-usage/
        ├── leader-ai-log.md
        ├── developer1-ai-log.md
        └── developer2-ai-log.md
```

## Branching Strategy

The team follows a lightweight Git Flow: `main` holds the stable release, `develop` is the integration branch, and every piece of work lives on its own branch, named with a prefix that describes its purpose:

- `feature/` for new modules (for example, `feature/accessory-module`).
- `fix/` for bug fixes (for example, `fix/duplicate-sellable-id`).
- `refactor/` for restructuring without changing behavior.
- `docs/` for documentation-only changes.

Everything merges into `develop` via Pull Request, and cross-review is mandatory — nobody approves their own PR. Commits are written in English, are atomic, and follow Conventional Commits. There are no direct commits to `main` or `develop`, and no force pushes. Branches are deleted from the remote once merged.

## Design Docs

- [Analysis](docs/analysis.md)
- [Hierarchy Diagram](docs/Hierarchy%20diagram.md)
- [Class Diagram](docs/Class%20diagram.md)
- [Layers Diagram](docs/Layer%20diagram.md)

## AI Usage Logs

- [Technical Lead](docs/ai-usage/leader-ai-log.md)
- [Developer 1](docs/ai-usage/developer1-ai-log.md)
- [Developer 2](docs/ai-usage/developer2-ai-log.md)
