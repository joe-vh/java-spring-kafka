package project.models;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class BaseInstrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
