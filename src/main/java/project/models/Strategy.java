package project.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;


@Data
@Entity
@JsonIgnoreProperties({ "conditions" })
public class Strategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "open")
    private Boolean open;

    @Column(name = "occurred")
    private Boolean occurred;

    @Column(name = "side")
    private Boolean side;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="future_id", nullable=true)
    private Future future;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="option_id", nullable=true)
    private Option option;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cfd_id", nullable=true)
    private Cfd cfd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="basket_id", nullable=false)
    @JsonBackReference
    private Basket basket;

    @OneToMany(cascade = ALL, mappedBy="strategy")
    @JsonManagedReference
    private List<Condition> conditions = new ArrayList<>();

    @Column(name = "timestamp")
    private Date timestamp;
}



