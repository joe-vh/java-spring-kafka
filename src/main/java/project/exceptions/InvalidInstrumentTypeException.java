package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid instrument type. Either 'future', 'option' or 'cfd' is accepted")
public class InvalidInstrumentTypeException extends RuntimeException {

}
