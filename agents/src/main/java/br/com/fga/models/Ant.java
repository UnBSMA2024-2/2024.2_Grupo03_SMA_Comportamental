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
public class Ant implements Serializable {

    @Serial
    private static final long serialVersionUID = -6838541954030003600L;

    private int positionX;

    private int positionY;

}
