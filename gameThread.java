import java.io.*;
import java.net.*;
import java.util.*;

public class gameThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String code;
    private int guessCount = 0;

    public gameThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            generateCode();


            String result = "    ";

            while (!result.equals("BBBB") && guessCount < 20) {
                if (guessCount == 0) {
                    out.println("GO");
                } else {
                    String guess = in.readLine();
                    if (guess.equalsIgnoreCase("QUIT")) break;

                    result = processGuess(guess);
                    out.println(result);
                }
                guessCount++;
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateCode() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(rand.nextInt(10));
        }
        code = sb.toString();
    }

    private String processGuess(String guess) {
        StringBuilder cs = new StringBuilder();
        StringBuilder bs = new StringBuilder();
        boolean[] codeUsed = new boolean[4];
        boolean[] guessUsed = new boolean[4];

        // First pass: Check for B's (correct digit and position)
        for (int i = 0; i < 4; i++) {
            if (guess.charAt(i) == code.charAt(i)) {
                bs.append("B");
                codeUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Second pass: Check for C's (correct digit, wrong position)
        for (int i = 0; i < 4; i++) {
            if (guessUsed[i]) continue;
            for (int j = 0; j < 4; j++) {
                if (!codeUsed[j] && guess.charAt(i) == code.charAt(j)) {
                    cs.append("C");
                    codeUsed[j] = true;
                    break;
                }
            }
        }

        // Build final result: C's first, then B's, then spaces
        StringBuilder result = new StringBuilder(cs.toString() + bs.toString());
        while (result.length() < 4) result.append(" ");

        return result.toString();
    }
}
