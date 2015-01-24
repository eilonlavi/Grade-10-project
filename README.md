# Grade-10-project
Tank game
Run the game from the MAIN class.
Press 0 to cheat and skip a level.
The generator class is a saparate, crude level design program.

Architecture:
MAIN keeps an array of GameObject subclass instances, and continuously cycles through them, handling requests.
For instance, some subclasses want to move, so they have "canMove" set to true, and a specific move function that main
will periodically invoke.
