package pl.lodz.p.it.ssbd2021.ssbd02.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Adnotacja pozwalająca przeprowadzić walidację pola requiredSpace klasy VehicleType
 *
 * @author Karolina Kowalczyk
 */
@Target( { FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VehicleRequiredSpaceValidator.class)
public @interface VehicleRequiredSpaceValidation {

    String message() default "Vehicle required space must be 0, 0.5, 1, 2 or 3";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
