## Introduction
This is a puzzle game set in a museum. 

You can interact with objects, which will open screens. You will know that an object is interactable if an outline appears around it when your character is close to it. Similarly, some screens have parts that are clickable, but will only show a hover-outline if you hover over it with your mouse.

In this game, there are three rooms excluding the library and the entrance hall. The library acts as a hub between the three rooms. The door to each room is opened by entering something into the Mayan calendar in the library. The date to enter into the calendar is obtained from the previous room.
## How to Play
Game Jar Download: [https://github.com/cometkaizo/PuzzleGame/raw/refs/heads/master/out/artifacts/PuzzleGame_jar/PuzzleGame.jar](url)

Simply run the jar file and press “New Game” to start a save file.
When you exit the game, your save file will automatically save and can be retrieved later by pressing “Load Game.”
“Debug Game” can be used to bypass puzzles for testing purposes.

Note: This program requires Java 25.
## Controls
### In the game
Movement: WASD

Interact / Open Inventory: E

Dash (for quick movement): SPACE
### In a screen overlay
Close the current screen overlay: E

Interact with something in the overlay: LEFT MOUSE BUTTON
### Debug controls
Toggle debug rendering: Z

Forcibly solve lock / forcibly open door: V
## About the Puzzles
The use of pencil and paper is recommended for taking notes as you play. A calculator may be needed for basic arithmetic.

Especially in rooms 2 and 3, the puzzles can be non-linear. This means that sometimes you may encounter puzzles that are impossible to solve without solving an earlier puzzle which you skipped.

The puzzles in this game do not require you to search anything up, or use any resource outside the game besides a pencil, paper, and calculator. If you feel like you need to search something up, it is probably not the answer.

## About Debug Mode
Although all the puzzles in this game have been playtested, some puzzles may still be too obscure. If this gets in the way of your experience of the game, you can close the window to save it, reload the game in debug mode, use debug controls to automatically solve the puzzle, and relaunch in regular mode.

Alternatively, you can use console commands to give items that are not obtainable through debug mode alone.

## Console Commands
Console commands can be used to perform actions in the game for debugging purposes. They can be performed regardless of whether you launched the game in debug mode. Console commands are only available if you start the program from the console, using java -jar PuzzleGame.jar

There are not many console commands available, but here they are:
### Give command
Usage:

give feather → gives you the feather of truth

give heart1 → gives you the heart of war

give heart2 → gives you the heart of love

give machine → gives you a machine piece

give notes → gives you all notes from the sculptures room

give organ_key → gives you an organ key
### Position command
Usage:

pos → prints the player’s current position
### Save command
Usage:

save → saves the game
### Exit command
Usage:

exit true → saves and exits

exit false → exits without saving
### Teleport command
Usage:

tp x y → teleports the player to (x, y)

