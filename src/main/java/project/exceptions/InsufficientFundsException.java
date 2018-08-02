package project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value=HttpStatus.UNPROCESSABLE_ENTITY,
        reason="Insufficient funds to complete this transaction. The client should not repeat this request without modification"
)
public class InsufficientFundsException extends RuntimeException {

}

