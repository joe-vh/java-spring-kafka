package project.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseMaturityTick extends BaseTick {
    @Column(name = "type")
    private String type;
    @Column(name = "maturity")
    private Date maturity;
    @Column(name = "t")
    private BigDecimal t;
    @Column(name = "strike")
    private BigDecimal strike;
    @Column(name = "r")
    private BigDecimal r;
    @Column(name = "vol")
    private BigDecimal vol;
    @Column(name = "delta")
    private BigDecimal delta;
    @Column(name = "gamma")
    private BigDecimal gamma;
    @Column(name = "vega")
    private BigDecimal vega;
    @Column(name = "theta")
    private BigDecimal theta;
    @Column(name = "rho")
    private BigDecimal rho;
}
