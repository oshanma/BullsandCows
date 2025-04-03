import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class gameDaemon {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        while (true) {
            Socket socket = serverSocket.accept();
            gameThread thread = new gameThread(socket);
            thread.start();
        }
    }
}