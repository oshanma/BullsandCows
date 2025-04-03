/**
 * The gameDaemon class serves as the server for the Bulls and Cows game.
 * It listens for incoming client connections on a specified port and
 * spawns a new gameThread to handle each connection.
 */
import java.net.ServerSocket;
import java.net.Socket;

public class gameDaemon {
    public static void main(String[] args) {
        int port = 12345; // Port number on which the server will listen for client connections
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println();
            System.out.println("CODE: " + port);// Display the port number for reference

            while (true) {
                // Accept an incoming client connection; this method blocks until a connection is made
                Socket socket = serverSocket.accept();
                // Create and start a new thread to handle the client's game session
                new gameThread(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();// Print any exceptions that occur to the standard error stream
        }
    }
}
