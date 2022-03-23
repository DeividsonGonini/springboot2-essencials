package academy.devdojo.springboot2.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder //por extender a classe ExceptionDetails
public class BadRequestExceptionDetails extends ExceptionDetails {

}
