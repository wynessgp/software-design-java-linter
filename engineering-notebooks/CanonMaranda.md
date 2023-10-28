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