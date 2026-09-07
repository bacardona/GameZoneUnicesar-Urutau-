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

For the complete rationale behind the layering, see [docs/Layer diagram.md](docs/Layer%20diagram.md). The full class-level design is in [docs/Class diagram.md](docs/Class%20diagram.md).

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

The [`data/`](./data) directory ships pre-populated with at least three sellers, since staff are treated as already on payroll and are never created through the UI. Every record — products, people, and sales — is written to disk after each operation and read back in automatically the next time the app starts.

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

7. Record a new sale (customer, seller, and one or more products).
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
    ├── hierarchy-diagram.md
    ├── class-diagram.md
    ├── layers-diagram.md
    └── ai-usage/
        ├── leader-ai-log.md
        ├── developer1-ai-log.md
        └── developer2-ai-log.md
```

## Branching Strategy

The team follows a lightweight Git Flow: `main` holds the stable release, `develop` is the integration branch, and each module lives on its own `feature/*` branch. Everything merges into `develop` via Pull Request, and cross-review is mandatory — nobody approves their own PR.

## Design Docs

- [Analysis](docs/analysis.md)
- [Hierarchy Diagram](docs/Hierarchy%20diagram.md)
- [Class Diagram](docs/Class%20diagram.md)
- [Layers Diagram](docs/Layer%20diagram.md)

## AI Usage Logs

- [Technical Lead](docs/ai-usage/leader-ai-log.md)
- [Developer 1](docs/ai-usage/developer1-ai-log.md)
- [Developer 2](docs/ai-usage/developer2-ai-log.md)
