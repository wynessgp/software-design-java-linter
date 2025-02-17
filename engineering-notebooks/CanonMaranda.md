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

## 11/6 10am-12pm
### Plan:
- Finish design
- Add configuration instructions to README

### Progress:
- Wrote configuration instructions
  - Provide directory by command line argument or in command line interface
  - Add example test files to implementation branch
- Update design
  - Recursive file loader
  - Ensure the design works
  - Begin adding private methods and fields to individual checks
- Starting implementation on Wednesday

## 11/8 10am-12pm
### Plan:
- Begin implementation

### Progress:
- Designed skeleton classes
  - Updated UML to reflect changes made
- Began functionality for ASM adapters and presentation layer
  - Changed getAccess() to a method that checks for a list of access modifiers
    - Plan to abstract it out

## 11/10 10-11am
### Plan:
- Begin work on recursive file search

### Progress:
- Implemented findClassFiles() in RecursiveDiver
  - Looks for all .class files in directory and adds paths to a set
  - To be parsed in ASM ClassReader
- Plan for ClassReader interface and adapter
  - Used to parse .class files, check for existance

## 11/11 7:30-10pm
### Plan:
- Update design to use facade pattern in domain layer
  - Handles creating ClassReaders and running checks

### Progress:
- Created LintRunner class
- Added functionality to ClassReader abstraction
  - Handles finding the right .class file, creating ClassNode object
- Program can successfully process .class files from a directory
  - Uses InputStream method rather than class names for ClassReader

## 11/12 2:30-3:30pm
### Plan:
- Prepare UI for running checks

### Progress:
- Implemented check options dialog
  - User is prompted for each check, asking if they would like to run it
  - Uses a list of Class\<?> objects, not sure how to improve flexibilty for this functionality
  - Ready to run selected checks once user presses the enter key

## 11/12 8-10pm
### Plan:
- Work on implementing checks
  - Start with static check
  - Update test cases

### Progress:
- Wrote algorithm for StaticMethodCheck
  - Same approach as before, looking for instances of classes where all public items are static
  - Provided updated test case (should fail the check)
  - Working on providing proper output

## 11/13 1-4:30pm
### Plan:
- Fix implementation of static check
  - Add new adapters/methods if necessary
- Work on remaining checks

### Progress:
- Updated arguments for faster debugging
  - Running `java Main <dir> <check-name> ...` will import the directory and select the provided checks
- Static check test produces correct output
  - Look for public, non-static methods and fields to determine non-static classes
  - Added methods to remove ASM types from check code
- Additional static check test added
- Update design
  - Using inheritance with InstructionNode adapter classes
- Continue working on remaining checks later today

## 11/13 6-10:30pm
### Plan:
- Implement information hiding and facade pattern checks

### Progress:
- Wrote information hiding check
  - No algorithmic change from previous implementation
- Updated how checks return "empty" results
  - UI expects a "no violations detected" message or something similar
  - If not, default "no results" text is displayed
- Wrote facade pattern check
  - Switched to searching for facade by package
  - Looks for a class that calls others, but nothing in the package calls it
  - A program's main class will always be detected as a facade
  - "Possibly" keyword added to results since the pattern has a loose definition in this check
- Wrote unit tests for my checks
  - All follow the same format
  - LintRunner and list of files are reset before each test
  - Use RecursiveDiver to parse directories with .class files
  - Make sure the expected number of files are found
  - Run checks and check for consistent results
- Begin implementing result saving
  - Saves to results.txt in the project root directory
  - Includes class names, check types, and check results
- Add Javadocs for complicated methods
- Wrote wiki pages for check algorithms

## 11/14 9:30-11am
### Plan
- Review PR with bulk of implementation
- Improve facade pattern check algorithm
- Small refactoring changes

### Progress
- Merged implementation and notebook branches into main
  - Resolved conflict in .gitignore file
- Updated facade check
  - If multiple classes in a package are detected as facades, then the pattern doesn't apply
  - Updating definition to say that only one class per package can be a facade
  - Updated wiki page with changes to definition
  - Wrote new test case for this scenario
- Method abstraction
  - MethodNodeASM and FieldNodeASM have a matchesAccess method, which checks access modifiers with an space-separated input string. The implementations are the same, so calling this functionality from a common location would result in a more modifiable design.
  - Also copy over getName and getDesc (also in LocalVariableNodeASM) to allow for modifying the return string format easily in the future
  - Not updating ClassNodeASM because its matchesAccess method is different
- Make PR with current changes (everything except extra feature)

## 11/14 2:30-5pm
### Plan:
- Work on code to UML generation

### Progress:
- Spent most of my time looking for a UML generation library. The one I found doesn't work with Maven.
  - Ignoring extra feature for now
- Change where the results output file is initialized
