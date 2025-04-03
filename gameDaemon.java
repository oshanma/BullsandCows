import java.net.ServerSocket;
import java.net.Socket;

public class gameDaemon {
    public static void main(String[] args) {
        int i = 12345; // Define the port number
        try (ServerSocket serverSocket = new ServerSocket(i)) {
            System.out.println();
            System.out.println("CODE: " + i);
            while (true) {
                Socket socket = serverSocket.accept();
                new gameThread(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
