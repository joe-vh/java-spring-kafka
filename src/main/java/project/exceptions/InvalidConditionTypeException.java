package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid condition type. '<', '==' or '>' are accepted")
public class InvalidConditionTypeException extends RuntimeException {

}
