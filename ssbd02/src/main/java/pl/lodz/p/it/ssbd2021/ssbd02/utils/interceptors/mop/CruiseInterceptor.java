package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli Cruise.
 *
 * @author Wojciech Sowa
 */
public class CruiseInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link CruiseExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("cruise_number_unique")) {
                throw CruiseExceptions.createConflictException(CruiseExceptions.ERROR_CRUISE_NUMBER_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("end_date_after_start_date")) {
                throw CruiseExceptions.createNotAcceptableException(CruiseExceptions.ERROR_CRUISE_END_DATE_AFTER_START_DATE);
            }

            throw pe;
        }
    }
}
