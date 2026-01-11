# Reversi+ - Extended Reversi Game

Reversi+ is a Java-based implementation of the classic Reversi (Othello) board game, enhanced with special disc mechanics and AI opponents.
The game runs on an 8×8 board, features a graphical user interface, and supports both human and computer-controlled players.

## Game Rules

- The objective is to finish with the highest number of discs in your color.
- The game begins with four discs in the center.
- Three types of discs exist: Regular, Unflippable, and Bomb (special flipping rules apply).
- The game ends when no legal moves are available for the next player.

### Reversi+ follows the core rules of traditional Reversi while introducing additional disc types that add strategic depth:

- Standard disc-flipping mechanics.
- Special discs with unique behaviors.
- Computer-controlled opponents with different play styles.
The objective of the game is the same as a standard Reversi game: finish the game with more discs of your color than your opponent.

### Discs
In addition to regular Reversi discs, the game includes special mechanics:
- **Simple Disc** - Regular disc that follows standard Reversi rules - (left click to place)
- **Unflippable Disc** - Special disc that cannot be flipped once placed - each player starts the game with 2 unflippable discs (hold "v" and press the left click mouse button to place).
- **Bomb Disc** - When flipped, it causes surrounding discs to flip, potentially triggering other bombs - each player starts the game with 3 bomb discs (hold "b" and press the left click mouse button to place a bomb disc).


### Computer controlled Players
- **RandomAI** - Randomly selects a legal move.
- **GreedyAI** - Chooses the move that flips the maximum number of opponent discs.

### How to Run
1. Ensure you have an updated Java SDK installed.
2. Clone or download this repository.
3. Compile and run the 'Main' class

Created by Adi Moskovich and Gal Vinter