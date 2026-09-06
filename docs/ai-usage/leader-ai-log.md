AI Usage Log — Technical Lead
This log records AI-assisted decisions taken by the Technical Lead role during the development of the GameZone Unicesar system.

Entry 1
Date: 2026-09-05, 12:56 PM
Tool used: Claude (web chat)

Reason for use: Set up the Git branching model (main/develop) with branch protection, and configure the initial Maven project structure with the four required layer packages.

Problem faced: The repository had just been created with no branches other than a default one, no NetBeans Maven project existed yet, and I did not know the exact PowerShell/Git commands and GitHub UI steps needed to create protected branches, resolve an unrelated-histories push rejection, agregar colaboradores con permiso de escritura, and reorganize the NetBeans default package into the com.gamezone four-layer structure (model, persistence, service, ui) required by the workshop.

Prompt used: "Configurar el proyecto Maven con el pom.xml inicial y la estructura de paquetes de las cuatro capas" (and follow-up troubleshooting prompts as errors appeared: push rejected, merge conflicts in .gitignore, PowerShell touch command not found, empty package folders not being tracked by Git).

Solution obtained and decision taken: Created and pushed main and develop, enabled "require pull request before merging" on both, made the repository public (required for evaluation and for branch protection to work on a free GitHub plan), added adbermudez and ljchavez1 as collaborators with write access, and renamed the default NetBeans package to com.gamezone with the four subpackages. Since Git does not track empty folders, I created placeholder classes inside each package so the structure would be committed, with the explicit decision that each developer would later rename their own placeholder class into the real domain class for their module using NetBeans' Refactor → Rename, rather than me creating the real classes myself. All changes were merged into develop through pull requests reviewed by a teammate, never by direct push, following the Git Flow restrictions defined in the workshop statement.

Entry 2
Date: 2026-09-05, 5:31 PM
Tool used: Claude (web chat)

Reason for use: Translate the team's own answers to the 11 orienting analysis questions from Spanish to English for docs/analysis.md, and get feedback on whether the reasoning in three specific answers was complete enough.

Problem faced: My team had already discussed and written the reasoning for all 11 questions in Spanish, but the deliverable requires the file in English, and I was not fully confident that our answers to questions 5 (Sale relationships), 8 (automatic inventory update), and 11 (allowed/forbidden layer dependencies) were complete enough compared to what the workshop statement was asking.

Prompt used: "Sin ponernos tan técnicos, que tal te parecen estas respuestas en español?" followed by "Entonces, que sugerencias me das para cada una de las respuestas?" and finally "Traduce tú las 11 al inglés y arma el analysis.md completo."

Solution obtained and decision taken: Based on the feedback, my team reworded our own answers to Q5 (distinguishing simple association for Customer/Seller from aggregation for Product), Q8 (naming the actual classes involved: Sale, Product, SaleService, ProductService/ProductRepository), and Q11 (adding the forbidden dependencies, which we had originally omitted). Once the team had rewritten those three answers ourselves in Spanish, the translation to English was done by the AI, since the reasoning and decisions were already ours; the AI only performed the language translation and Markdown formatting of content the team had already produced and validated.

Entry 3
Date: 2026-09-05, 6:22 PM
Tool used: Claude (web chat)

Reason for use: Debug Mermaid syntax errors in the hierarchy, class, and layer diagrams, and get the correct Mermaid syntax to add two missing dependency relationships (Service→Model and Persistence→Model) that were identified as required by our own docs/analysis.md answer to question 11.

Problem faced: My first attempt at the hierarchy diagram rendered extra malformed classes because the code had been flattened into a single line when copy-pasted; a later attempt using an AI-diagram tool (mermaid.ai) hit a paid "line limit" wall instead of a real syntax problem; and once I had drawn my own class diagram and layers diagram, I did not know the exact Mermaid syntax to add invisible layout links (~~~) or dependency arrows (..>) without breaking the existing structure.

Prompt used: "Con respecto a la fase 1... necesito que me ayudes a como usar Mermaid para hacer el diagrama de jerarquías" and later "Aquí está el otro código [layers diagram]" while asking for the missing Service→Model dependency to be added correctly.

Solution obtained and decision taken: Switched from mermaid.ai to the free mermaid.live editor to remove the artificial line limit and the unpredictable AI-driven interpretation of the code. Learned the classDiagram syntax for abstract classes (<<abstract>>), inheritance (<|--), association/aggregation with multiplicity ("1" -->, "1" o--), and dependency (..>) arrows, and added six dependency lines (ProductService/PersonService/SaleService ..> their respective model classes, and the three repositories ..> their respective model classes) that I had designed based on our own analysis answers. Also replaced the linkStyle-by-index technique in the layers diagram with the more robust ~~~ invisible-link syntax. The class hierarchy, attributes, methods, and relationships themselves were designed by the team beforehand; the AI's role was limited to Mermaid syntax troubleshooting and rendering-tool guidance.

Entry 4
Date: 2026-09-05, 6:46 PM
Tool used: Claude (web chat)

Reason for use: Assemble the TEAM.md file with the team's roles, class distribution, and committed activities, in the format required by the workshop deliverable.

Problem faced: I needed to consolidate information that was already decided (team members, roles, module assignments) into a single Markdown file with the exact sections the workshop requires, and the "committed activities" section needed to mirror the activity lists already given in the workshop statement itself, translated to English and adapted to our actual class names.

Prompt used: "hazlo pero, también deseo que me expliques que fue lo que hiciste" (after providing full names and student codes for all three team members).

Solution obtained and decision taken: Built TEAM.md with the member table, the module/class distribution matching our already-approved class-diagram.md, and the feature branch names already in use in our repository. The committed-activities lists were taken directly from the workshop statement's own "Actividades del Líder Técnico / Desarrollador 1 / Desarrollador 2" sections (not invented), only translated to English and adapted to our real class names, since the workshop itself prescribes those activities. I moved the resulting file to the repository root (not inside docs/) after initially placing it in the wrong folder, and merged it into develop through a reviewed pull request.

Entry 5
Date: 2026-09-05, 9:30 PM
Tool used: Claude (web chat)

Reason for use: Get conceptual guidance on how to structure the Sale domain class (attributes, relationship to Product quantities) before implementing it myself, and clarify which of the four layers I am personally responsible for as Technical Lead.

Problem faced: The class-diagram.md defines Sale's relationship to Product as an aggregation with multiplicity 1..*, but does not specify how to represent the quantity of each product sold, and I was unclear on whether ConsoleUI could be built before Developer 1 and Developer 2 finished their modules, since it needs to call PersonService and ProductService methods that did not exist yet.

Prompt used: "Bueno, si ese es el caso y ya entendí, empecemos con el paso a paso que me habías dado" (after asking for clarification on my own role across the four layers).

Solution obtained and decision taken: The AI explained two design options for tracking product quantity per sale (a plain product list vs. an intermediate SaleItem class) without writing the final class for me, and I chose to move forward implementing Sale, SaleRepository, and SaleService myself in NetBeans. For ConsoleUI, the AI recommended building the menu skeleton using the exact method signatures already agreed upon in class-diagram.md, accepting that it would not compile until develop was updated with the merged Product and Person modules, rather than waiting idle for my teammates to finish. No complete class implementations were requested from or provided by the AI; all Java code for the Sale module was written by me based on this guidance.
