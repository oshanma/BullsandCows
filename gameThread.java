import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * The gameThread class handles the server-side logic for a single game session
 * of Bulls and Cows. It manages client communication, generates the secret code,
 * and processes user guesses.
 */
public class gameThread extends Thread {
    private Socket socket;           // Client socket connection
    private BufferedReader in;       // Input stream from client
    private PrintWriter out;         // Output stream to client
    private String code;             // Secret code to be guessed
    private int guessCount = 0;      // Number of guesses made by the client

    /**
     * Constructor to initialize the game thread with the client socket.
     *
     * @param socket The client socket connection
     */
    public gameThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * The run method contains the main game loop, handling client communication,
     * generating the secret code, and processing guesses.
     */
    @Override
    public void run() {
        try {
            // Initialize input and output streams for client communication
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Generate the secret code for this game session
            generateCode();

            String result = "    "; // Initialize result with four spaces

            // Main game loop: continue until the client guesses correctly or reaches 20 attempts
            while (!result.equals("BBBB") && guessCount < 20) {
                if (guessCount == 0) {
                    out.println("GO"); // Send start signal to client
                } else {
                    String guess = in.readLine(); // Read client's guess
                    if (guess.equalsIgnoreCase("QUIT")) break; // Exit if client chooses to quit

                    result = processGuess(guess); // Process the guess and get feedback
                    out.println(result); // Send feedback to client
                }
                guessCount++;
            }

            socket.close(); // Close the socket connection after the game ends
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a random 4-digit secret code with digits ranging from 0 to 9.
     * Digits may repeat.
     */
    private void generateCode() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(rand.nextInt(10));
        }
        code = sb.toString();
    }

    /**
     * Processes the client's guess by comparing it to the secret code and
     * returns feedback in the form of 'B' (correct digit and position),
     * 'C' (correct digit, wrong position), or spaces.
     *
     * @param guess The client's 4-digit guess
     * @return A 4-character string providing feedback on the guess
     */
    private String processGuess(String guess) {
        StringBuilder cs = new StringBuilder(); // Stores 'C' feedback
        StringBuilder bs = new StringBuilder(); // Stores 'B' feedback
        boolean[] codeUsed = new boolean[4];    // Tracks matched positions in code
        boolean[] guessUsed = new boolean[4];   // Tracks matched positions in guess

        // First pass: Check for 'B's (correct digit and position)
        for (int i = 0; i < 4; i++) {
            if (guess.charAt(i) == code.charAt(i)) {
                bs.append('B');
                codeUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Second pass: Check for 'C's (correct digit, wrong position)
        for (int i = 0; i < 4; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < 4; j++) {
                    if (!codeUsed[j] && guess.charAt(i) == code.charAt(j)) {
                        cs.append('C');
                        codeUsed[j] = true;
                        break;
                    }
                }
            }
        }

        // Combine 'C's and 'B's, then pad with spaces to ensure a 4-character response
        StringBuilder result = new StringBuilder(cs.toString()).append(bs.toString());
        while (result.length() < 4) {
            result.append(' ');
        }

        return result.toString();
    }
}
