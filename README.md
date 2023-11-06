# Project: Linter

## Dependencies
Specified in pom.xml

## Contributors
Canon Maranda, Bee Cerasoli, Grant Wyness

## Team Member's Engineering Notebooks (one per person)
- Canon Maranda: [Engineering Notebook](engineering-notebooks/CanonMaranda.md)
- Bee Cerasoli: [Notebook](https://docs.google.com/document/d/1_xj7S6hVaAqizzn7vlWLSlxKkKVMPZ8B_MMFa4dZQrw/edit?usp=sharing)
- Grant Wyness: [Engineering Notebook](engineering-notebooks/GrantWyness.md)

## Features
| Developer     | Style Check    | Principle Check     | Pattern Check     | A Feature (optional)   |
|:--------------|:---------------|:--------------------|:------------------|:-----------------------|
| Canon Maranda | Static methods | Information hiding  | Facade pattern    | Code to UML            |
| Bee Cerasoli  | Formatting     | 3 Layer Principle   | Strategy Pattern  | UML to code            |
| Grant Wyness  | Unused "items" | Hollywood Principle | Decorator Pattern | Unused "item" deletion |

## Configuration
This program requires the user to provide a directory containing source code binaries for the linter to check. The user can specify a starting directory in two ways:

1. As a command line argument
   - Either a complete or arbitrary path. Checks to ensure .class files exist when the program begins.
   - Example: `java -jar linter.jar path/to/binaries`
3. After being prompted by the command line interface
   - Either a complete or arbitrary path. Does not display if the user entered a command line path. Checks to ensure .class files exist before continuing.
   - Example: `java -jar linter.jar`<br>
            Enter a directory path: `path/to/binaries`

The program will recursively search for .class files within the provided directory. After the binary files are loaded, the user can select which check to run. These checks are displayed to the console upon completion. A prompt to save the checks to a .txt file is also displayed. Lastly, the user can choose to apply code or UML transformations.