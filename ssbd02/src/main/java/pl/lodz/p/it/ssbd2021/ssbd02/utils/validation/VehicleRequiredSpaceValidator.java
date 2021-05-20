package pl.lodz.p.it.ssbd2021.ssbd02.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa implementująca mechanizm walidacji pól.
 *
 * @author Karolina Kowalczyk
 */
public class VehicleRequiredSpaceValidator implements ConstraintValidator<VehicleRequiredSpaceValidation, Double> {

    List<Double> validValues = Arrays.asList(0D, 0.5, 1D, 2D, 3D);

    /**
     * Metoda sprawdzająca poprawność przekazanych danych
     *
     * @param requiredSpace przekazana wartość przestrzeni zajmowanej przez dany typ pojazdu
     * @return True, jeśli wartość zawiera się w zbiorze prawidłowych wartości, w przeciwnym wypadku false
     */
    @Override
    public boolean isValid(Double requiredSpace, ConstraintValidatorContext constraintValidatorContext) {

        return validValues.contains(requiredSpace);
    }
}
