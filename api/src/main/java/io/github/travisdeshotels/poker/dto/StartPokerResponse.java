package io.github.travisdeshotels.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartPokerResponse {
    String gameId;
    String hostId;
}
