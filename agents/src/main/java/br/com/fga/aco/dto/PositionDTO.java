package br.com.fga.aco.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PositionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 333581581860320717L;

    @JsonProperty("x")
    private Integer x;

    @JsonProperty("y")
    private Integer y;

}
