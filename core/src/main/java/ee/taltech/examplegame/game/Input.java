package ee.taltech.examplegame.game;

import com.badlogic.gdx.Gdx;
import ee.taltech.examplegame.network.ServerConnection;
import message.Direction;
import message.PlayerMovementMessage;

public class Input {
    public void handleMovementInput() {
        var movementMessage = new PlayerMovementMessage();

        // detect key presses and send movement message to the server
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            // send movement message to the server
            movementMessage.setDirection(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            // send movement message to the server
            movementMessage.setDirection(Direction.RIGHT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            // send movement message to the server
            movementMessage.setDirection(Direction.UP);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            // send movement message to the server
            movementMessage.setDirection(Direction.DOWN);
        }

        // don't send anything if player is not moving
        if (movementMessage.getDirection() == null) return;

        ServerConnection
            .getInstance()
            .getClient()
            .sendUDP(movementMessage);
    }
}
