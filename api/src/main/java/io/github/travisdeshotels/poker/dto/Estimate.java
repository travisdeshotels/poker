package io.github.travisdeshotels.poker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Estimate {
    String playerId;
    String pointValue;
}
