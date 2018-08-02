package project.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRequest {
	@NotBlank
	private String username;
}
