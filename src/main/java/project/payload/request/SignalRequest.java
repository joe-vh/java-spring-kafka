package project.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignalRequest {
	@NotBlank
	private String name;
	@NotBlank
	private String url;
}
