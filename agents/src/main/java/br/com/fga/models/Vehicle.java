package br.com.fga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class Vehicle implements Serializable {

    @Serial
    private static final long serialVersionUID = 2031264515899704974L;

    private UUID id;
    private String name;
    private Double speed;
    private String color;
    private City currentCity;
}
