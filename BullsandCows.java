import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BullsandCows {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String serverMsg = in.readUTF();
            if (serverMsg.equals("GO")) {
                System.out.println("Welcome to Bulls and Cows. You will try to guess a 4 digit code using\n" +
                        "only the digits 0-9. You will lose the game if you are unable to guess\n" +
                        "the code correctly in 20 guesses. Good Luck!");
            }

            int guesses = 0;
            String response = "";
            while (guesses < 20 && !response.equals("BBBB")) {
                String guess = getValidGuess();
                if (guess.equals("QUIT")) {
                    //out.writeUTF(guess);
                    out.flush();
                    System.out.println("Answer:" + guess);
                    System.out.println("Goodbye but please play again!");
                    socket.close();
                    return;
                }
                out.writeUTF(guess);
                out.flush();
                guesses++;
                response = in.readUTF();
                System.out.println(guess + " " + response.replace(' ', '_'));
                if (response.equals("BBBB")) {
                    System.out.println("Congratulations!!! You guessed the code correctly in " + guesses + " guesses");
                    socket.close();
                    return;
                }
            }
            if (guesses == 20) {
                System.out.println("Sorry – the game is over. You did not guess the code correctly in 20 moves.");
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getValidGuess() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your guess for the secret code or “QUIT” : ");
            String input = scanner.nextLine().trim();
            if (verifyInput(input)) {
                return input;
            } else {
                System.out.println("Improperly formatted guess.");
            }
        }
    }

    private static boolean verifyInput(String gs) {
        if (gs.equals("QUIT")) {
            return true;
        }
        if (gs.length() != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            char c = gs.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}