import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class gameThread extends Thread {
    private Socket socket;

    public gameThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String code = generateCode();
            System.out.println("CODE: " + code);
            out.writeUTF("GO");
            out.flush();

            int counter = 0;
            String response = "";
            while (counter < 20 && !response.equals("BBBB")) {
                String guess = in.readUTF();
                if (guess.equals("QUIT")) {
                    break;
                }
                response = processGuess(guess, code);
                out.writeUTF(response);
                counter++;
                if (response.equals("BBBB")) {
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    private String processGuess(String guess, String code) {
        int bulls = 0;
        int cows = 0;
        boolean[] codeMatched = new boolean[4];
        boolean[] guessMatched = new boolean[4];

        for (int i = 0; i < 4; i++) {
            if (code.charAt(i) == guess.charAt(i)) {
                bulls++;
                codeMatched[i] = true;
                guessMatched[i] = true;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (guessMatched[i]) continue;
            for (int j = 0; j < 4; j++) {
                if (codeMatched[j]) continue;
                if (guess.charAt(i) == code.charAt(j)) {
                    cows++;
                    codeMatched[j] = true;
                    guessMatched[i] = true;
                    break;
                }
            }
        }

        StringBuilder response = new StringBuilder();
        for (int i = 0; i < cows; i++) response.append('C');
        for (int i = 0; i < bulls; i++) response.append('B');
        while (response.length() < 4) response.append(' ');
        return response.toString();
    }
}