package project.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class CfdTick extends BaseTick {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cfd_tick_generator")
    @SequenceGenerator(name = "cfd_tick_generator", sequenceName = "cfd_tick_sequence")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cfd_id", nullable=false)
    @JsonBackReference
    private Cfd cfd;
    @Column(name="cfd_id", updatable=false, insertable=false)
    private Long cfd_id;
    @Column(name = "buy")
    private BigDecimal buy;
    @Column(name = "sell")
    private BigDecimal sell;
    @Column(name = "spread")
    private BigDecimal spread;
}
