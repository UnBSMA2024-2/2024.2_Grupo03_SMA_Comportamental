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
public class Position implements Serializable {

    @Serial
    private static final long serialVersionUID = -3614224928523365746L;

    private Double x;
    private Double y;
}
