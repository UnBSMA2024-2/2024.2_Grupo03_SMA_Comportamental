package br.com.fga.models;

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
public class Simulation implements Serializable {
    @Serial
    private static final long serialVersionUID = -6125833600009859542L;

    private Position position;
    private Vehicle vehicle;

}
