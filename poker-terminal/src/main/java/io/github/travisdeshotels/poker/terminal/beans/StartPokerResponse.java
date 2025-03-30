package io.github.travisdeshotels.poker.terminal.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartPokerResponse {
    String gameId;
    String hostId;
}
