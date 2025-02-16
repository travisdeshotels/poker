package io.github.travisdeshotels.poker.terminal.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinResponse {
    String playerId;
    int numberOfPlayersConnected;
}
