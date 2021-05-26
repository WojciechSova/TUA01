package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.FerryExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli Ferry.
 *
 * @author Wojciech Sowa
 */
public class FerryInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link FerryExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("ferry_name_unique")) {
                throw FerryExceptions.createConflictException(FerryExceptions.ERROR_FERRY_NAME_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("vehicle_capacity_not_negative")) {
                throw FerryExceptions.createNotAcceptableException(FerryExceptions.ERROR_FERRY_VEHICLE_CAPACITY_NOT_NEGATIVE);
            }

            if (pe.getCause().getCause().toString().contains("on_deck_capacity_greater_than_zero")) {
                throw FerryExceptions.createNotAcceptableException(FerryExceptions.ERROR_FERRY_ON_DECK_CAPACITY_GREATER_THAN_ZERO);
            }

            throw pe;
        }
    }
}
