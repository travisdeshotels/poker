package io.github.travisdeshotels.poker.terminal.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandStatus {
    int playersWithoutEstimate;
    int playersTotal;
    List<Estimate> estimateList;
    Float estimateAverage;
}
