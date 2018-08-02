package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Price out of range. The client should not repeat this request without modification")
public class PriceOutOfRangeException  extends RuntimeException {

}
