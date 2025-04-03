/*
CSC 460 
Program #2 [Client-Server (multi threaded) Bulls and Cows]
Name: Oshan Maharjan
 */

/**
 * The BullsandCows class represents the client-side implementation of the Bulls and Cows game.
 * It connects to the game server, facilitates user interaction, and processes server responses.
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BullsandCows {
    public static void main(String[] args) {
        final int port = 12345; // Server port number

        try (
                // Establish connection to the server
                Socket socket = new Socket("localhost", port);
                // Input stream to receive data from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Output stream to send data to the server
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // Scanner to read user input from the console
                Scanner scanner = new Scanner(System.in)
        ) {
            int guessCount = 0; // Counter for the number of guesses made
            String guess = "";  // Stores the user's current guess

            // Game loop: continues until the user has made 20 guesses or guesses the correct code
            while (guessCount < 20) {
                String serverMsg = in.readLine(); // Read message from the server

                if ("GO".equals(serverMsg)) {

                    // Initial server message to start the game
                    System.out.println();
                    System.out.println("Welcome to Bulls and Cows. You will try to guess a 4-digit code using");
                    System.out.println("only the digits 0-9. You will lose the game if you are unable to guess");
                    System.out.println("the code correctly in 20 guesses. Good Luck!");
                    System.out.println();
                } else {
                    // Display the user's guess and the server's response
                    System.out.println(guess + " " + serverMsg);

                    if ("BBBB".equals(serverMsg)) {
                        // User has guessed the correct code
                        System.out.println();
                        System.out.println("Congratulations!!! You guessed the code correctly in " + guessCount + " guesses.");
                        break;
                    }
                }

                // Prompt the user for their next guess
                do {
                    System.out.print("Please enter your guess for the secret code or “QUIT” : ");
                    guess = scanner.nextLine().trim();

                    if ("QUIT".equalsIgnoreCase(guess)) {
                        // User chooses to quit the game
                        System.out.println();
                        System.out.println("Goodbye, but please play again!");
                        out.println("QUIT");
                        return;
                    }
                } while (!verifyInput(guess)); // Repeat until a valid input is provided

                out.println(guess); // Send the valid guess to the server
                guessCount++;       // Increment the guess counter
            }

            if (guessCount == 20) {
                // User has reached the maximum number of guesses
                System.out.println();
                System.out.println("Sorry – the game is over. You did not guess the code correctly in 20 moves.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean verifyInput(String input) {
        if ("QUIT".equalsIgnoreCase(input)) {
            return true;
        }
        if (input.length() != 4) {
            System.out.println("Improperly formatted guess.");
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                System.out.println("Improperly formatted guess.");
                return false;
            }
        }
        return true;
    }
}
