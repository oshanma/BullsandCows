import java.net.ServerSocket;
import java.net.Socket;

public class gameDaemon {
    public static void main(String[] args) {
        int port = 12345; // Define the port number
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println();
            System.out.println("CODE: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new gameThread(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
