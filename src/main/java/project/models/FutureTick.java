package project.models;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class FutureTick extends BaseMaturityTick {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "future_tick_generator")
    @SequenceGenerator(name = "future_tick_generator", sequenceName = "future_tick_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="future_id", nullable=false)
    @JsonBackReference
    private Future future;
    @Column(name="future_id", updatable=false, insertable=false)
    private Long future_id;
    @Column(name = "future_price")
    private BigDecimal futurePrice;
}
