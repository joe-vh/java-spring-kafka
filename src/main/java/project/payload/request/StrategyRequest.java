package project.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class StrategyRequest {
	@NotNull(message = "Please enter name")
	private String name;

	@NotNull(message = "Please enter basket id")
	private Long basketId;

	@NotNull(message = "Please enter quantity")
	private Integer quantity;

	private Long futureId;

	private Long optionId;

	private Long cfdId;

	@NotNull(message = "Please enter side")
	private Boolean side;

	@NotNull(message = "Please enter open")
	private Boolean open;

	@NotNull(message = "Please add conditions")
	private List<ConditionTemplate> conditions;
}
