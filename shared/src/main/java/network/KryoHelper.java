package network;

import com.esotericsoftware.kryo.Kryo;

public class KryoHelper {

    public static void registerClasses(Kryo kryo) {
        // all classes that you want to send over the network
        // must be registered here. To make the handling of these classes
        // easier they are all stored in the "messages" package
        kryo.register(message.GameJoinMessage.class);
        kryo.register(message.PlayerMovementMessage.class);
        kryo.register(message.Direction.class);
        kryo.register(message.GameStateMessage.class);
        kryo.register(message.PlayerState.class);
    }
}
