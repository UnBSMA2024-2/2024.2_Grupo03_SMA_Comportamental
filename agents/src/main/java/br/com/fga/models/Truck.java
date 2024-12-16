package br.com.fga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Truck extends Vehicle {

    @Serial
    private static final long serialVersionUID = 5753535523474185359L;

    private Integer capacity;

    public Truck(Double speed, String color, Integer capacity) {
        super(UUID.randomUUID(), speed, color);
        this.capacity = capacity;
    }
}
