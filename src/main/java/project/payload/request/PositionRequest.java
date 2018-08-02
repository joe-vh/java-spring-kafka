package project.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PositionRequest {
	@NotNull(message = "Missing basketId")
	private Long basketId;
	@NotNull(message = "Missing instrumentId")
	private Long instrumentId;
	@NotNull(message = "Missing tickId")
	private Long tickId;
	@NotNull(message = "Missing quantity")
	private Integer quantity;
	@NotNull(message = "Missing price")
	private BigDecimal price;
	@NotNull(message = "Missing type")
	private String type;
	private String kind;
	private String maturity;
	private Boolean side;
}
