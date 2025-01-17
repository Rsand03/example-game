package ee.taltech.examplegame.server.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static constant.Constants.ARENA_LOWER_BOUND_X;
import static constant.Constants.ARENA_LOWER_BOUND_Y;
import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;
import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;

public class BulletCollisionManager {


    public List<Bullet> handleCollisions(List<Bullet> bullets, List<Player> players) {
        List<Bullet> bulletsToBeRemoved = new ArrayList<>();

        for (Player player : players) {
            Rectangle hitBox = constructPlayerHitBox(player);
            for (Bullet bullet : bullets) {
                if (bullet.getShotById() != player.getId() && hitBox.contains(bullet.getX(), bullet.getY())) {
                    player.decreaseLives();
                    bulletsToBeRemoved.add(bullet);
                }
            }
        }
        bulletsToBeRemoved.addAll(findOutOfBoundsBullets(bullets));
        bullets.removeAll(bulletsToBeRemoved);
        return bullets;
    }

    private List<Bullet> findOutOfBoundsBullets(List<Bullet> bullets) {
        List<Bullet> outOfBoundsBullets = new ArrayList<>();
        for (Bullet b : bullets) {
            if (b.getX() < ARENA_LOWER_BOUND_X || b.getX() > ARENA_UPPER_BOUND_X ||
                b.getY() < ARENA_LOWER_BOUND_Y || b.getY() > ARENA_UPPER_BOUND_Y) {
                outOfBoundsBullets.add(b);
            }
        }
        return outOfBoundsBullets;
    }

    private Rectangle constructPlayerHitBox(Player player) {
        return
            new Rectangle(
                (int) (player.getX()),
                (int) (player.getY()),
                (int) PLAYER_WIDTH_IN_PIXELS,
                (int) PLAYER_HEIGHT_IN_PIXELS
            );
    }

}
