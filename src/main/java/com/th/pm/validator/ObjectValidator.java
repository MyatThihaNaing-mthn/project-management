package com.th.pm.validator;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.th.pm.exceptions.InvalidObjectException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class ObjectValidator<T> {
    private Validator validator;

    public ObjectValidator( Validator validator){
        this.validator = validator;
    }

    public void doValidation(T objectToValidate){
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);
        if(!violations.isEmpty()){
            String errorMessage = violations.stream().map(
                ConstraintViolation::getMessage
            ).collect(Collectors.joining("\n"));
            throw new InvalidObjectException(errorMessage);
        }
    }
}
