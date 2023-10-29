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
