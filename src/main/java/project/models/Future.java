package project.models;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import static javax.persistence.CascadeType.ALL;


@Data
@Entity
@JsonIgnoreProperties({ "futureTicks" })
public class Future extends BaseInstrument {
    @OneToMany(cascade = ALL, mappedBy = "future")
    private List<FutureTick> futureTicks = new ArrayList<>();
}
