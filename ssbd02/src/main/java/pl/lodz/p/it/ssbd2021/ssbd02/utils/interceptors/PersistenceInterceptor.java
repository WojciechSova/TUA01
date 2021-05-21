package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

/**
 * Klasa interceptora przechwytująca wyjątki związane z bazą danych niezależne od tabeli, w której wystąpią.
 *
 * @author Kacper Świercz
 */
public class PersistenceInterceptor {


    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej lub {@link CommonExceptions}
     */
    @AroundInvoke
    public Object intercept(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (OptimisticLockException ole) {
            throw CommonExceptions.createOptimisticLockException();
        } catch (NoResultException nre) {
            throw CommonExceptions.createNoResultException();
        } catch (JDBCConnectionException jce) {
            throw CommonExceptions.createJDBCConnectionException();
        }
    }
}
