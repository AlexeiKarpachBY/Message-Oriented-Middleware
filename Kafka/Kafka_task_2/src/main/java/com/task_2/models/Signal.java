package com.task_2.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class Signal {

    private boolean isValid;
    private Long distance;

    @JsonCreator
    public Signal(@JsonProperty("isValid") boolean isValid,
                  @JsonProperty("distance") Long distance) {
        this.isValid = isValid;
        this.distance = distance;
    }

}
