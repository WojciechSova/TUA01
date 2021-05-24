package pl.lodz.p.it.ssbd2021.ssbd02.utils.interceptors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.List;

/**
 * Klasa interceptora tworząca wpisy do dziennika zdarzeń o wywołanych metodach, ich parametrach, co zwróciły oraz kto je wywołał.
 *
 * @author Wojciech Sowa
 */
public class TrackerInterceptor {

    @Resource
    private SessionContext sctx;
    private static final Logger logger = LogManager.getLogger();

    /**
     * Metoda przechwytująca wywołanie metod, w klasie gdzie użyty został ten interceptor.
     *
     * @param ictx Kontekst wywołania.
     * @return Wynik wywołania metody, którą przechwycił interceptor.
     * @throws Exception Wyjątek złapany w metodzie przechwyconej
     */
    @AroundInvoke
    public Object traceInvoke(InvocationContext ictx) throws Exception {
        StringBuilder message = new StringBuilder("The captured method call: ");
        Object result;
        try {
            try {
                message.append(ictx.getMethod().toString());
                message.append(" authenticated user: ").append(sctx.getCallerPrincipal().getName());
                message.append(" values of the parameters: ");
                if (null != ictx.getParameters()) {
                    for (Object param : ictx.getParameters()) {
                        if (param instanceof AbstractEntity) {
                            message.append(param.getClass().getName())
                                    .append(((AbstractEntity) param).getSummary());
                        } else {
                            message.append(String.valueOf(param)).append(" ");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Unexpected exception in interceptor code: ", e);
                throw e;
            }

            result = ictx.proceed();

        } catch (Exception e) {
            message.append(" terminated by exception: ").append(e.toString());
            logger.error(message.toString(), e);
            throw e;
        }

        message.append(" value returned: ");
        if (null == result) {
            message.append("null ");
        } else if (result instanceof List) {
            for (Object obj : ((List<?>) result).toArray()) {
                if (obj instanceof Pair) {
                    message.append(messageFromPair(obj));
                } else {
                    message.append(obj.getClass().getName())
                            .append(((AbstractEntity) obj).getSummary());
                }
            }
        } else if (result instanceof Pair) {
            message.append(messageFromPair(result));
        } else if (result instanceof AbstractEntity) {
            message.append(result.getClass().getName())
                    .append(((AbstractEntity) result).getSummary());
        } else {
            message.append(result.toString()).append(" ");
        }

        logger.info(message.toString());

        return result;
    }

    private String messageFromPair(Object result) {
        StringBuilder message = new StringBuilder();
        Pair pair = (Pair) result;
        if (pair.getLeft() instanceof List) {
            for (Object obj : ((List<?>) pair.getLeft()).toArray()) {
                message.append(obj.getClass().getName())
                        .append(((AbstractEntity) obj).getSummary());
            }
        } else {
            message.append(pair.getLeft().getClass().getName())
                    .append(((AbstractEntity) pair.getLeft()).getSummary());
        }
        if (pair.getRight() instanceof List) {
            for (Object obj : ((List<?>) pair.getRight()).toArray()) {
                message.append(obj.getClass().getName())
                        .append(((AbstractEntity) obj).getSummary());
            }
        } else {
            message.append(pair.getRight().getClass().getName())
                    .append(((AbstractEntity) pair.getRight()).getSummary());
        }
        return message.toString();
    }
}
