package project.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BasketRequest {
	@NotBlank
	private String name;
}
