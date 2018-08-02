package project.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class ConditionTemplate {
	@NotNull(message = "Please enter field")
	private String field;

	@NotNull(message = "Please enter condition")
	private String condition;

	@NotNull(message = "Please enter value")
	private BigDecimal value;

	private Boolean backwardation;

	private Long futureId;

	private Long optionId;

	private Long cfdId;
}
