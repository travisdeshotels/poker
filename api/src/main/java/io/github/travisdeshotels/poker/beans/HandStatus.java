package io.github.travisdeshotels.poker.beans;

import lombok.Data;

import java.util.List;

@Data
public class HandStatus {
    int playersWithoutEstimate;
    int playersTotal;
    List<Estimate> estimateList;
    Float estimateAverage;
}
