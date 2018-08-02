package project.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Entity
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="strategy_id", nullable=true)
    @JsonBackReference
    private Strategy strategy;

    @Column(name="strategy_id", updatable=false, insertable=false)
    private Long strategy_id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="future_id", nullable=true)
    private Future future;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="option_id", nullable=true)
    private Option option;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cfd_id", nullable=true)
    private Cfd cfd;

    @Column(name = "field")
    private String field;

    @Column(name = "condition")
    private String condition;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "backwardation")
    private Boolean backwardation;

    @Column(name = "occurred")
    private Boolean occurred;

    @Column(name = "timestamp")
    private Date timestamp;
}
