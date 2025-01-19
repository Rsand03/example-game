package ee.taltech.examplegame.server.game;

import com.esotericsoftware.minlog.Log;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static constant.Constants.ARENA_LOWER_BOUND_X;
import static constant.Constants.ARENA_LOWER_BOUND_Y;
import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;
import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;

public class BulletCollisionHandler {


    public List<Bullet> handleCollisions(List<Bullet> bullets, List<Player> players) {
        List<Bullet> bulletsToBeRemoved = new ArrayList<>();

        for (Player player : players) {
            Rectangle hitBox = constructPlayerHitBox(player);
            for (Bullet bullet : bullets) {
                // register a hit only if the bullet was shot by a different player
                if (bullet.getShotById() != player.getId() && hitBox.contains(bullet.getX(), bullet.getY())) {
                    player.decreaseLives();
                    bulletsToBeRemoved.add(bullet);
                    Log.info("Player with id " + player.getId() + " was hit. " + player.getLives() + " lives left.");
                }
            }
        }
        bulletsToBeRemoved.addAll(findOutOfBoundsBullets(bullets));
        bullets.removeAll(bulletsToBeRemoved);  // remove bullets that hit a player or move out of bounds
        return bullets;
    }

    private List<Bullet> findOutOfBoundsBullets(List<Bullet> bullets) {
        List<Bullet> outOfBoundsBullets = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.getX() < ARENA_LOWER_BOUND_X ||
                bullet.getX() > ARENA_UPPER_BOUND_X ||
                bullet.getY() < ARENA_LOWER_BOUND_Y ||
                bullet.getY() > ARENA_UPPER_BOUND_Y
            ) {
                outOfBoundsBullets.add(bullet);
            }
        }
        return outOfBoundsBullets;
    }

    private Rectangle constructPlayerHitBox(Player player) {
        return
            new Rectangle(
                (int) (player.getX()),  // bottom left corner coordinates
                (int) (player.getY()),  // bottom left corner coordinates
                (int) PLAYER_WIDTH_IN_PIXELS,  // rectangle width
                (int) PLAYER_HEIGHT_IN_PIXELS  // rectangle height
            );
    }

}
