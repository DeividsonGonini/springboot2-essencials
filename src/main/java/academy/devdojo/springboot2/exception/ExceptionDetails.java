package academy.devdojo.springboot2.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder//por ser extendida para outras classes
public class ExceptionDetails {
    protected String title;
    protected int status;
    protected String datails;
    protected String developerMessage;
    protected LocalDateTime timestamp;

}
