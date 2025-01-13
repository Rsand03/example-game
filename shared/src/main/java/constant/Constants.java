package constant;

public class Constants {
    // --- networking constants ---
    public static final int PORT_TCP = 54555;
    public static final int PORT_UDP = 54777;
    // this should be changed depending on where the server is hosted
    public static final String SERVER_IP = "localhost";
    // --- player constants ---
    public static final float PLAYER_SPEED = 2.0f;
    public static final int PLAYER_LIVES_COUNT = 5;
    public static final float PLAYER_HEIGHT_IN_PIXELS = 32;
    public static final float PLAYER_WIDTH_IN_PIXELS = 16;
    // --- bullet constants ---
    public static final float BULLET_SPEED = 5.0f;
    public static final long BULLET_TIMEOUT_IN_MILLIS = 200;
    // --- game constants ---
    public static final int GAME_TICK_RATE = 60;

}
