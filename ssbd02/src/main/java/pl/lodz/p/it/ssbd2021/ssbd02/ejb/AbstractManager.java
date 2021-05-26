package pl.lodz.p.it.ssbd2021.ssbd02.ejb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.SessionSynchronization;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa abstrakcyjna managera. Przeznaczona jako klasa nadrzędna dla klas managerów.
 * Zapewnia implementacje metod z interfejsu {@link SessionSynchronization}.
 * Zapewnia zapisywanie do dzienników zdarzeń cyklu życia transakcji.
 *
 * @author Kacper Świercz
 */
public abstract class AbstractManager {

    protected static final Logger logger = LogManager.getLogger();
    protected boolean transactionRolledBack = false;
    @Context
    protected SecurityContext securityContext;
    private String transactionId;

    /**
     * Metoda tworząca unikalny identyfikator transakcji.
     * Do dzienników zdarzeń zapisywany jest początek transakcji wraz z znacznikiem czasu, identyfikator transakcji,
     * metodą w której rozpoczynana jest transakcja oraz tożsamością uwierzytelnionego użytkownika.
     * W przypadku braku tożsamości użytkownika zapisywana jest tożsamość "ANONYMOUS".
     */
    public void afterBegin() {
        transactionId = Long.toString(System.currentTimeMillis()) + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

        logger.info("Transaction with ID {} started in {}, authenticated user: {}",
                transactionId, this.getClass().getName(), getInvokerId());
    }

    /**
     * Metoda zapisująca do dzienników zdarzeń informację o zbliżającym się zakończeniu transakcji wraz z
     * znacznikiem czasu, identyfikator transakcji, metodą w której rozpoczynana jest transakcja oraz
     * tożsamością uwierzytelnionego użytkownika.
     * W przypadku braku tożsamości użytkownika zapisywana jest tożsamość "ANONYMOUS".
     */
    public void beforeCompletion() {
        logger.info("Transaction with ID {} is about to end in {}, authenticated user: {}",
                transactionId, this.getClass().getName(), getInvokerId());
    }

    /**
     * Metoda zapisująca do dzienników zdarzeń informację zakończonej transakcji wraz z znacznikiem czasu,
     * identyfikator transakcji, metodą w której rozpoczynana jest transakcja, tożsamością uwierzytelnionego użytkownika oraz
     * statusem zakończonej transakcji (zatwierdzona lub odwołana).
     * W przypadku braku tożsamości użytkownika zapisywana jest tożsamość "ANONYMOUS".
     *
     * @param committed Status zakończenia transakcji
     */
    public void afterCompletion(boolean committed) {
        logger.info("Transaction with ID {} ended in {} with status: {}, authenticated user: {}",
                transactionId, this.getClass().getName(), getStatusMsg(committed), getInvokerId());
        this.transactionRolledBack = !committed;
    }

    /**
     * Metoda zwracająca wartość łańcucha znakowego określającego status transakcji, który ma zostać zapisany do dzienników zdarzeń.
     *
     * @param committed Status transakcji
     * @return Łańcuch znakowy zapisywany do dzienników zdarzeń
     */
    private String getStatusMsg(boolean committed) {
        return committed ? "COMMITTED" : "ROLLED BACK";
    }

    /**
     * Metoda zwracająca tożsamość uwierzytelnionego użytkownika.
     * W przypadku braku tożsamości użytkownika zwracana jest tożsamość "ANONYMOUS".
     *
     * @return Tożsamość użytkownika
     */
    protected String getInvokerId() {
        if(securityContext == null){
            return "ANONYMOUS";
        }
        return securityContext.getUserPrincipal() != null ?
                securityContext.getUserPrincipal().getName() : "ANONYMOUS";
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean isTransactionRolledBack() {
        return transactionRolledBack;
    }
}
