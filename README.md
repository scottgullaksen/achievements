# achievements
A general java implementation of achievements which can be used in games

# Implementation: Code description/usage
For a description on why we have achievements in the game and how they are intended to work, see #221.

## A UML Overview for the implementation
![AchievementUML](uploads/0fc1265077e912b8d79a71df272abf75/AchievementUML.png)

### The achievement class
Implements the java.util.Observer interface. This is because we recognized the possibility of using the Oberver-observed design pattern when using achievements and stats. Achievements should be able to keep track of and update themselves whenever changes on relevant stats are made(i.e. they observe). This also fully encapsulates achievement objects. 

It has a set of constraints (objects) that it is initialized with. These are used to access relevant stats to set itself as an observer and to measure if constraints (rules) are satisfied.

Only when all constraints are satisfied is the achievement unlocked.

## The Constraint class
Consists of a type of rule (bigger, smaller, equals), a treshold value and the stat used for measuring. Purely data-oriented. Only created with the creation of achievements. A constraint may be used by several achievements.

## The stat class
Represents a user metric to be measured and updated when playing the game. Extends the Observable class. When it's value is changed, the stat can notify observers, i.e. achievements that this particular stats value has changed. This allows achievements to check if new constraints has been satisfied, i.e. progression towards unlocking it.

## The achieve class
Is a singleton java object. This is because only one of this object should be active in the game. This object instanciates every single achievement and stat obtainable in the game when the game starts, and manages them. It functions as a black box. I.e. use this object whenever you need to change a stat. All updates are managed from within that class and it's components. 

The class can also be used by the User class and UI classes to get access unlocked or all achievements, and stats.

**Note**: This means that the achieve class is specific to the user playing, in the sense that it keeps track of a specific users progress on stats and achievements. However, it is also general in the sense that it represents and provides you with all achievements and stats that are obtainable in the game.
