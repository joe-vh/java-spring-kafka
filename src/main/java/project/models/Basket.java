package project.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;


@Data
@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private User user;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade=ALL, mappedBy="basket")
    @JsonManagedReference
    @Transient
    private List<Position> positions = new ArrayList<>();

    @OneToMany(cascade=ALL, mappedBy="strategy")
    @JsonManagedReference
    @Transient
    private List<Strategy> strategies = new ArrayList<>();
}
