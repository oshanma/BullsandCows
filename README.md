CSC 460 – Program #2
Author: Oshan Maharjan
Class: CSC-460 (Operating Systems)
Professor: Dr. Newell
Assignment: Program #2 – Bulls and Cows (Multithreaded Client-Server Game)

--------------------------------------------------------------------------------
Description:
--------------------------------------------------------------------------------

This project is a multithreaded client-server Java implementation of the game
**Bulls and Cows**. The server listens for incoming connections on port 12345
and spawns a new thread for each client that connects. Each client attempts to
guess a randomly generated 4-digit code (digits 0-9, repeats allowed) within
20 tries.

- 'B' stands for a correct digit in the correct position.
- 'C' stands for a correct digit in the wrong position.
- ' ' (blank) indicates the digit is not part of the code.

The server ends the game for the client either upon a correct guess ("BBBB")
or after 20 attempts.

--------------------------------------------------------------------------------
Files:
--------------------------------------------------------------------------------

1. `gameDaemon.java`
   - The main server class that listens on port 12345.
   - Accepts new client connections in an infinite loop.
   - Spawns a new `gameThread` for each client connection.

2. `gameThread.java`
   - A thread class handling the game session for each individual client.
   - Generates the secret code and provides feedback for each guess.
   - Terminates the game on success, "QUIT", or 20 attempts.

3. `BullsandCows.java`
   - The client program that connects to the server at `localhost:12345`.
   - Reads user input, sends guesses, receives and displays feedback.
   - Enforces input validation and handles game-ending messages.

--------------------------------------------------------------------------------
How to Compile and Run:
--------------------------------------------------------------------------------

1. Compile all files:

   ```bash
   javac gameDaemon.java gameThread.java BullsandCows.java
