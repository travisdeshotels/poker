package io.github.travisdeshotels.poker.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstimateWithPlayerName{
    String playerName;
    String pointValue;
}
