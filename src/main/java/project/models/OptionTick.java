package project.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class OptionTick extends BaseMaturityTick {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_tick_generator")
    @SequenceGenerator(name = "option_tick_generator", sequenceName = "option_tick_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="option_id", nullable=false)
    @JsonBackReference
    private Option option;
    @Column(name="option_id", updatable=false, insertable=false)
    private Long option_id;
    @Column(name = "spot")
    private BigDecimal spot;
}
