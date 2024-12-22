package br.com.fga.aco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeAdjacentDTO {

    @JsonProperty("node")
    private String node;

    @JsonProperty("distance")
    private Integer distance;
}
