package project.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import static javax.persistence.CascadeType.ALL;


@Data
@Entity
@JsonIgnoreProperties({ "optionTicks" })
public class Option extends BaseInstrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade=ALL, mappedBy="option")
//    @JsonManagedReference
    private List<OptionTick> optionTicks = new ArrayList<>();
}
