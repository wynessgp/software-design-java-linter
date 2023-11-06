## 10/23 10am-12pm 
### Plan: 
- Set up team repository
- Coordinate future meeting times with team
- Get initial ideas for checks, and A level feature

### Progress:
- Team repository set up, all team members joined
- Planned on meeting during regular class times (in CS lab, if regular class not hosted), Saturdays 3-5PM.
- Decided on looking for the following checks:
    - Unusued "items" (variables, potentially methods, potentially parameters)
    - Hollywood Principle
    - Decorator Pattern
    - Feature: Automatic unused item deletion (toggleable, but will remove code)

## 10/25 10-12pm
### Plan:
- Define & get project architecture reviewed
- Do initial brainstorming & research on check algos

### Progress:
- Architecture defined [view here](https://github.com/rhit-csse374/project-202410-team10-202410/wiki/Architecture), reviewed by in-class TA
- List of thoughts/discoveries on algorithms:
    - Unused "items"
        - For variables, just keep a list of global variable names when starting analysis. Then, look through methods and ensure the variable name comes up (in a meaningful context), otherwise store it with results.
        - For unused parameters, just keep a list of those as well. Shouldn't be too bad.
        - Methods might be a bit more difficult, as I'll have to analyze other classes if a method isn't private. 
    - Hollywood Principle
        - If we see a getter for a class that is utilized in a comparison outside of the respective class, then we likely have a violation.
    - Decorator Pattern
        - Relies on the existence of two key classes, and an additional one to sort of represent the actual decoration.
        - Looking for the existence of a class that both extends and has a instance of another class, and implements the same method as the parent. Finally, the decorator subclass should have the same method, but with some behavioral differences.

## 10/26 10am-12pm
### Plan:
- Do some small work on quick implementation of algorithms 
- Get some decent work in on the UML design

### Progress:
- Unfortunately, not much work done on algos today. Got sucked into design.
- Set up groundwork for UML [view here](../design.puml)
- Decided on strategy patterns for the main checks, as we anticipate them having similar parameters and returns. 
- Will be using an adapter pattern to give users the idea that different representations may be used for methods in static code, but we will be using ASM's library representations for actual analysis.
- Set up loaders/loggers for data source in order to represent different potential options for output from the linter and input.

## 10/28 3-4pm
### Plan: 
- Continue UML design work
    - None really done this meeting
- Start actual check implementation as algorithms should be isolated with strategy pattern being used.

### Progress:
- Managed to get the idea of checking local variables working, but need to do more fine-tuning as it should really only flag if it's initialized to a value but never used afterwards.
- Not much done in terms of other two checks yet. Decorator shouldn't be too bad; neither should Hollywood - there's just a bit of difficulty from the fact that they rely on having multiple classes as input.

## 10/30 10:30am-12pm
### Plan:
- Continue implementation work on basic algos, better form than ASM Example was given in

### Progress:
- Unused "items"
    - Checks for private fields, private methods working correctly
    - Checks for local variables within a method working correctly
    - Public/protected/default seem to be a little more difficult than anticipated
        - Need to have "hooks" that say we're anticipating this method be used after analyzing a class
    - Should be finished by Wednesday before class
- Hollywood Principle
    - No current implementation progress
    - Anticipate that once I get public/protected checking down with unused "items" this should follow super quickly
    - Plan is to see if a field is private, then see if a getter is utilized to make a comparison
    - Should be finished by Wednesday before class
- Decorator Pattern
    - No current implementation progress
    - Need some familiarity with chains between classes, again should follow from unused "items"
    - Should be finished by Wednesday before class (hopefully)
- Do mostly design work on Wednesday, so we can get the parts put together
- Looked into building tests.

## 11/1 11am-12pm
### Plan:
- Discuss commonalities between algorithms to try and incorperate it into design
- Work on design

### Progress: 
- Removed strategy pattern for three different styles of checks, consolidated to a single abstract class
- Abstracted the various parses that we do for classes, methods, instructions, fields.
    - Still need to work out parameters
- A little more work done on algos

## 11/2 10am-12pm
### Plan:
- Work on design

### Progress
- Abstracted the domain layer so that other libraries could be used, but enabled ourselves to properly use ASM on the backend
- Still need to finalize presentation layer
    - I suspect this will have less to do with my algorithms (as they just need results printed, largely) and will have more to do with my teammate's algos/features

## 11/4 6pm-11pm
### Plan:
- Nail down my algorithms so they can be directly put into the domain layer, once all of our design is decided
- Write more test cases

### Progress
- Graph traversal to properly check public/protected fields & methods now working for unused items
    - Code needs heavy refactoring to get it into proper shape
- "Happy path" for decorator pattern implemented
    - Need to work on graph traversals for this too
    - Otherwise, we won't be able to do checks if classes are not passed in appropriate order
- Hollywood Pattern still proving a little elusive (although other algos have stolen my time for the most part)

## 11/5 1pm-3pm
### Plan:
- Get my "graph" traversals for decorator pattern working
    - i.e. Cases where the classes are passed in but not in ideal order

### Progress:
- Decorator pattern check now works independently of order classes are passed in
- Makes check for abstract component and abstract decorator
    - If those two do not exist, then a decorator pattern can't possibly exist
    - If they do, then we print out the classes that have "potentially been identified" as part of the pattern


