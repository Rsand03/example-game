package ee.taltech.examplegame.server;

import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

import static constant.Constants.PORT_TCP;
import static constant.Constants.PORT_UDP;

/**
 * Launches the server application.
 */
public class ServerLauncher {

    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        Server server = new Server();
        server.start();
        try {
            server.bind(PORT_TCP, PORT_UDP);
        } catch (IOException e) {
            System.out.println("Server failed to start. Error: " + e.getMessage());
        }
    }
}
