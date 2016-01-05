package com.balaji.goeuro.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Position {
    @JsonProperty("_id")
    private String id;
    private String name;
    private String type;

    @JsonProperty("geo_position")
    private GeoPosition geoPosition;
}
