## 10/23 10am-12pm
### Plan:
- Create repository
- Plan meeting times
- Brainstorm feature ideas

### Progress:
- Created repository
- Plan to meet during class, Saturdays 3-5pm
- Working on the following checks:
  - Static methods
  - Information hiding
  - Facade pattern

## 10/25 10am-12pm
### Plan:
- Define project architecture
- Begin experimenting with check algorithms

### Progress:
- Defined [project architecture](https://github.com/rhit-csse374/project-202410-team10-202410/wiki/Architecture)
- Looked into potential algorithms for static method, information hiding, and facade pattern checks

## 10/26 10am-12pm
### Plan:
- Begin work on algorithms
- Begin work on UML design

### Progress:
- Static method check algorithm:
  - Look at each method call
  - If the method is static and the caller uses a class definition, show an error
- Information hiding check algorithm:
  - Show an error if a field is public (or package private)
- Facade pattern check algorithm:
  - Look for class that calls many methods from other classes, but no classes call its methods
  - Potentially check by package
- Created initial [UML diagram](../design.puml)

## 10/28 3-4pm
### Plan:
- Discuss initial design from 10/26
- Continue work on basic algorithm implementations
  - Main priority, have algorithm ideas ready before design is implemented

### Progress:
- Looked over design
  - Good start
  - Will continue on Monday
- Continue looking at basic algorithm implementations (outside of project directory)
  - Static method check will generate a list of all methods declared as static
    - Look through all of code to find method calls
    - If a static method is called using an instantiated object, show an error
  - Information hiding check will generate a list of all fields with public visibility
    - Consider showing errors when a field is package private
  - Facade pattern check will find all class dependencies
    - Show an error if a single class does not handle inner method calls
    - Consider checking by package

## 10/30 10:30am-12pm
### Plan:
- Implement basic algorithms

### Progress:
- Work on algorithms
  - Static opcodes are the same when accessing statically or through an object
    - Look into a way around this
  - Everything else should be doable (without looking at source code)
  - Finish all but static methods by Wednesday before class
- Continue design work on Wednesday

## 10/31 10-11am
### Plan:
- Finish information hiding and facade pattern algorithms

### Progress:
- Information hiding
  - Look at each variable in a class
  - If it's public, tell the user that information hiding is violated
- Facade pattern
  - Look at everything in each class
    - Superclasses and interfaces
    - Declared fields
    - Method parameters and return types
    - Locally declared variables
  - Map each external class occurrence to the current class
    - Ignore Java objects and primitive types
  - After the entire project is parsed
    - Duplicate the set of classes
    - If a class ever maps to a value inside the new set, remove it
    - The remaining classes are facades

## 11/1 11am-12pm
### Plan:
- Discuss algorithm implementations
- Continue work on design

### Progress:
- Update design based on what is needed for algorithms
  - Pattern to abstract linter checks, parsing types
  - Continue design work during next meeting

## 11/2 10am-12pm
### Plan:
- Finalize design

### Progress:
- Redefine static check
  - Should not define instance of an object that only has public static methods
  - Create algorithm by Monday
- Continue work on design
  - Create bytecode abstraction tree
  - Separate console and file writing
  - Need to work on presentation layer

## 11/4 8:30-10pm
### Plan:
- Finish new static check algorithm
- Work on presentation layer of design

### Progress:
- Added static check algorithm to example code
  - Additional modifiers check with Opcodes.ACC_* + Opcodes.ACC_*
  - Ignore initialization methods (\<init>, \<clinit>)
  - Remove non-static classes from list of visited classes
  - Recurse through field declarations
  - If a declaration is from a "static" class, flag it
- Updated presentation layer
  - Abstract UI and input class structure
  - User input performs the following functions:
    - Get working directory
      - Future: parse directory in domain layer
    - Ask to enable/disable checks
    - Display results
    - Ask to save results
    - Ask to perform additional features
      - Code cleanup (unused vars)
      - Code to UML
      - UML to code