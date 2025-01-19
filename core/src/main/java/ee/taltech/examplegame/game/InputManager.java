package ee.taltech.examplegame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.screen.TitleScreen;
import message.Direction;
import message.PlayerMovementMessage;
import message.PlayerShootingMessage;

public class InputManager {

    public void handleNavigationInput(Game game) {
        // navigate to TitleScreen
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new TitleScreen(game));
        }
    }

    public void handleMovementInput() {
        var movementMessage = new PlayerMovementMessage();

        // detect key presses and send a movement message with the desired direction to the server
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            movementMessage.setDirection(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            movementMessage.setDirection(Direction.RIGHT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            movementMessage.setDirection(Direction.UP);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            movementMessage.setDirection(Direction.DOWN);
        }

        // on't send anything if player is not moving
        if (movementMessage.getDirection() == null) return;

        ServerConnection
            .getInstance()
            .getClient()
            .sendUDP(movementMessage);
    }

    public void handleShootingInput() {
        var shootingMessage = new PlayerShootingMessage();

        // detect key presses and send shooting message to the server
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            shootingMessage.setDirection(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            shootingMessage.setDirection(Direction.RIGHT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            shootingMessage.setDirection(Direction.UP);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            shootingMessage.setDirection(Direction.DOWN);
        }

        // don't send anything if player is not shooting
        if (shootingMessage.getDirection() == null) return;

        ServerConnection
            .getInstance()
            .getClient()
            .sendUDP(shootingMessage);
    }
}
