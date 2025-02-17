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

## 11/6 10am-12pm
### Plan:
- Write configuration details
- Work towards finalizing design

### Progress:
- Configuration details now written on [README](/README.md)
- Design is as finalized as can be without beginning implementation
    - Plan is to update individual checks with private fields/methods as we go
    - Test classes will also be moved as we go

## 11/8 10am-12pm
### Plan:
- Setup the skeleton for our classes
- Begin adding fields as necessary to have code work

### Progress:
- Skeleton setup for classes
- Some implementation of presentation and datasource layers done
    - Need to have recursive file diver set up for JUnit tests (if needed)
- Continue implementation into Thursday & the weekend

## 11/9 10am-12pm
### Plan:
- Do some work on implementation with new design
- Defer some work to weekend

### Progress:
- Some implementation for the important skeleton ASM classes
- Some more design work done to fix issues that we found.

## 11/11 6:30pm-10pm 
### Plan:
- Get the remaining skeleton classes implemented (within reason)
- Refactor Decorator pattern, the only fully moved one in the project so far
- Do some research on A level feature
- Side note: work logs from here on out will be on Central Time (I live in NW Indiana, and have gone home for the quarter)

### Progress:
- Remaining skeleton classes have implementation done
- Decorator pattern is fully integrated with adapter pattern for ASM
    - Still need to decide what we will do with ASM's types.
- Discovered how to actually delete methods and fields, but local variables will have to remain
    - Nasty bug with looping variables being declared as unused
    - Can't get rid of looping variables without knowing scope of the loop, which is hard
- Looking to do more work tonight, but it may carry over into Sunday (11/12), so this is the commit.

## 11/12 6pm-11pm?
### Plan:
- Move over Hollywood Principle algorithm to main project
- Add necessary classes as needed to support its functionality
- Write wiki pages for algos

### Progress:
- Hollywood Principle algo fully integrated into Linter
    - Checks and other testing code not yet written, but will be moved soon
- Added variable instruction nodes, made small updates to method and field instruction nodes
    - Wanted to make string checking consistent with ASM, although it didn't really matter
- Wiki pages written for currently integrated algorithms.
- (I am writing this journal entry with hindsight)

## 11/13 4pm-11:59pm 
### Plan:
- Get test code working for already moved checks (decorator, hollywood)
- Move unused items check over & refactor it, write test cases for it.
- Implement supporting classes as necessary
- Update design as necessary

### Progress:
- Test code written for decorator/hollywood
    - Strong focus on using contains as opposed to checking strict structure on results
    - Note that since they use toString for sets and the like, there was bound to be permutations which would mess up a strict .equals check
    - More complex tests for Decorator, basic ones for hollywood (pass/fail).
- Updated design for decorator to reflect new methods and fields being added
    - There are a lot of private methods that all do small things
- Changed ClassNode to have isAbstract() and isInterface()
    - Slightly more flexible, meant to get around a big bug with how our matchesAccess worked, specifically with ClassNodes
        - Some weirdness with it having "open" as a modifier as well, and interfaces were more like "abstract interface"
- Note: work continued over into 11/14 in the early hours of the morning.

## 11/14 12am-3am
### Plan:
- Get unused items algo up and running on the Linter
- Write test cases for unused items algo
- Update design as necessary

### Progress:
- Unused items algorithm is fully supported
- There are test cases to go with it, one fail and one pass
- Design reflects updated methods used for unused items check, and fields.
    - Side note: might need to make additional revisions to facilitate deleting items
- Pleased with our progress! Canon has made the UI very functional while I kind of just stalk around the domain layer.
- He's had more experience, so I tend to just let him have his way.

## 11/14 11am-1pm
### Plan:
- Run a couple more tests using interactivity to ensure everything works OK
- Fix any associated bugs that might come up

### Progress:
- Had very bad bugs upon running the full domain through UnusedItems
    - Local variable code ended up messing up a LOT of things
    - Made it to where empty HashSets are generated for every class for:
        - methods
        - fields
        - interfaces
    - The map I had for local variables was causing me headaches, so I scrapped local variable support.
    - In its stead, I added support for interface methods being used and then them getting used in the associated implementing classes.
        - Hence the empty list of interfaces. It makes parsing easier.
- Fixed my UnusedItems test cases to accomodate for local variables no longer being tracked
    - Just remove one assertEquals line
- Wiki page updated to reflect that local variables are no longer included in output, but interfaces are tracked

## Design Decisions
- Make the adapter pattern for the ASM library within our project into an interface tree (idea spearheaded by Bee).
    - It just makes most things a little simpler only having to use methods and being guaranteed at least some usable semblence of results. We still do a lot of string replacing for "/" to ".", but that's our internal representation.
- Removed local variable tracking from UnusedItems
    - Having a double stacked map isn't exactly a great idea, plus removing it later would not be super easy to do (namely, we might accidentally get rid of looping variables instead)
    - Added interface support in its place for method tracking
- Made all of the checks follow a strategy pattern
    - Enables easy running of checks & picking specific ones from our domain layer facade
    - You can invoke the interface's methods without worry that it'll call something incorrectly
- Made a facade called LintRunner in our domain layer (idea spearheaded by Canon).
    - This facade provides just one entry point for the UI class instead of having the UI go everywhere
    - Makes it to where we can generate checks and ClassNodes just once instead of constantly having to throw different IOExceptions everywhere.
    - Also provides a good spot to do any last minute modification to output from checks if necessary








