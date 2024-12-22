package br.com.fga.aco.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Vector;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeDTO {

    @JsonProperty("node")
    private String node;

    @JsonProperty("edges")
    private Vector<NodeAdjacentDTO> edges = new Vector<>();

}
