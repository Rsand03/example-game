package ee.taltech.examplegame.server.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import ee.taltech.examplegame.server.game.Game;
import message.GameJoinMessage;


/**
 * This class listens for all connections and messages that are
 * sent to the server by the clients.
 * <p>
 * It contains 3 methods that can be overridden to add custom logic
 */
public class ServerListener extends Listener {
    private Game game;

    /**
     * When a client connects to the server, this method is called.
     * Include any logic that should be executed when a client connects to the server.
     * ex - add the client to the game, etc.
     *
     * @param connection The connection object that is created when a client connects to the server.
     */
    @Override
    public void connected(Connection connection) {
        Log.info("Client connected: " + connection.getRemoteAddressTCP().getAddress().getHostAddress());

        super.connected(connection);
    }

    /**
     * When a client disconnects from the server, this method is called.
     * Include any logic that should be executed when a client disconnects from the server.
     * ex - clean up resources, remove the client from the game, etc.
     *
     * @param connection The connection object of the client that disconnected.
     */
    @Override
    public void disconnected(Connection connection) {
        Log.info("Client disconnected: " + connection.getRemoteAddressTCP().getAddress().getHostAddress());

        game.removeConnection(connection);

        super.disconnected(connection);
    }

    /**
     * When a message is received from a client, this method is called.
     * ex - client sends a JoinGame message, the server will add the client to the game.
     *
     * @param connection The connection object of the client that sent the message.
     * @param object     The object that is sent by the client.
     */
    @Override
    public void received(Connection connection, Object object) {
        Log.debug("Received message from client (" + connection.getRemoteAddressTCP().getAddress().getHostAddress() + "): " + object.toString());

        // when a GameJoin message is received, the server will add the
        // connection to a game instance
        if (object instanceof GameJoinMessage) {
            game.addConnection(connection);
            if (!game.isGameRunning()) {
                game = new Game();
                game.start();
            }
        }

        super.received(connection, object);
    }
}
