package io.github.travisdeshotels.poker.dto;

import io.github.travisdeshotels.poker.beans.HandStatus;
import lombok.Data;

import java.util.List;

@Data
public class HandStatusDto {
    String handStatus;

    public HandStatusDto(HandStatus status){
        if (status == HandStatus.WAITING_ON_YOU){
            handStatus = "Waiting on player";
        } else if (status == HandStatus.WAITING_ON_OTHERS){
            handStatus = "Waiting on others";
        } else {
            handStatus = "Result is ready";
        }
    }
}
