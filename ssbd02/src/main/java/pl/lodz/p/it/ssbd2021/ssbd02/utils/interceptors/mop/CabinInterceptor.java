package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CabinExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli Cabin.
 *
 * @author Wojciech Sowa
 */
public class CabinInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link CabinExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("cabin_ferry_number_unique")) {
                throw CabinExceptions.createConflictException(CabinExceptions.ERROR_CABIN_FERRY_NUMBER_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("capacity_greater_than_zero")) {
                throw CabinExceptions.createNotAcceptableException(CabinExceptions.ERROR_CABIN_CAPACITY_GREATER_THAN_ZERO);
            }

            throw pe;
        }
    }
}
