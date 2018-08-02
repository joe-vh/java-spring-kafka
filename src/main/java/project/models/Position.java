package project.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="future_id", nullable=true)
    @JsonBackReference
    private Future future;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="option_id", nullable=true)
    @JsonBackReference
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cfd_id", nullable=true)
    @JsonBackReference
    private Cfd cfd;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="future_tick_id", nullable=true)
    private FutureTick futureTick;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="option_tick_id", nullable=true)
    private OptionTick optionTick;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cfd_tick_id", nullable=true)
    private CfdTick cfdTick;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="basket_id", nullable=false)
    @JsonBackReference
    private Basket basket;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "side")
    private Boolean side;

    @Column(name = "timestamp")
    private Date timestamp;
}
