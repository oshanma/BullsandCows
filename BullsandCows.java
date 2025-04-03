import java.io.*;
import java.net.*;
import java.util.Scanner;

public class BullsandCows {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            int guessCount = 0;
            String guess = "";

            while (guessCount < 20) {
                String serverMsg = in.readLine();
                if (serverMsg.equals("GO")) {
                    System.out.println();
                    System.out.println("Welcome to Bulls and Cows. You will try to guess a 4 digit code using only the digits 0-9.");
                    System.out.println("You will lose the game if you are unable to guess the code correctly in 20 guesses. Good Luck!");
                    System.out.println();
                } else {
                    System.out.println(guess + " " + serverMsg);
                    if (serverMsg.equals("BBBB")) {
                        System.out.println();
                        System.out.println("Congratulations!!! You guessed the code correctly in " + guessCount + " guesses");
                        break;
                    }
                }

                do {
                    System.out.print("Please enter your guess for the secret code or “QUIT” : ");
                    guess = scanner.nextLine().trim();
                    if (guess.equalsIgnoreCase("QUIT")) {
                        System.out.println();
                        System.out.println("Goodbye but please play again!");
                        out.println("QUIT");
                        return;
                    }
                } while (!verifyInput(guess));

                out.println(guess);
                guessCount++;
            }

            if (guessCount == 20) {
                System.out.println();
                System.out.println("Sorry – the game is over. You did not guess the code correctly in 20 moves.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean verifyInput(String gs) {
        if (gs.equals("QUIT")) return true;
        if (gs.length() != 4) {
            System.out.println("Improperly formatted guess.");
            return false;
        }
        for (char c : gs.toCharArray()) {
            if (!Character.isDigit(c)) {
                System.out.println("Improperly formatted guess.");
                return false;
            }
        }
        return true;
    }
}
