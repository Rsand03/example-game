package message;

import lombok.Data;

import java.util.List;

@Data
public class GameStateMessage {
    private List<PlayerState> playerStates;
}
