package project.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseTick {
    @Column(name = "timestamp")
    private Date timestamp;
    @Column(name = "price")
    private BigDecimal price;
}
