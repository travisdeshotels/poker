package io.github.travisdeshotels.poker.dto;

import io.github.travisdeshotels.poker.beans.EstimateWithPlayerName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HandResult {
    List<EstimateWithPlayerName> estimateList;
    Float estimateAverage;
}
