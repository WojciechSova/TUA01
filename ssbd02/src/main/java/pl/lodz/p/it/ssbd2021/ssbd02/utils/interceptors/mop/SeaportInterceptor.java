package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors.mop;

import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.SeaportExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z naruszeniem ograniczeń unikalności w tabeli Seaport.
 *
 * @author Wojciech Sowa
 */
public class SeaportInterceptor {

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link SeaportExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (PersistenceException pe) {

            if (pe.getCause().getCause().toString().contains("city_unique")) {
                throw SeaportExceptions.createConflictException(SeaportExceptions.ERROR_SEAPORT_CITY_UNIQUE);
            }

            if (pe.getCause().getCause().toString().contains("seaport_code_unique")) {
                throw SeaportExceptions.createConflictException(SeaportExceptions.ERROR_SEAPORT_CODE_UNIQUE);
            }

            throw pe;
        }
    }
}
