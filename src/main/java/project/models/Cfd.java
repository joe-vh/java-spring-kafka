package project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;


@Data
@Entity
@JsonIgnoreProperties({ "cfdTicks" })
public class Cfd extends BaseInstrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade=ALL, mappedBy="cfd")
//    @JsonManagedReference
    private List<CfdTick> cfdTicks = new ArrayList<>();
}
