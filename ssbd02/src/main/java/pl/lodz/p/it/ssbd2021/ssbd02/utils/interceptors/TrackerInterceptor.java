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

public class TrackerInterceptor {

    @Resource
    private SessionContext sctx;
    private static final Logger logger = LogManager.getLogger();

    @AroundInvoke
    public Object traceInvoke(InvocationContext ictx) throws Exception {
        StringBuilder message = new StringBuilder("The captured method call: ");
        Object result;
        try {
            try {
                message.append(ictx.getMethod().toString());
                message.append(" authenticated user: ").append(getInvokerId());
                message.append(" values of the parameters: ");
                if (null != ictx.getParameters()) {
                    for (Object param : ictx.getParameters()) {
                        if (param instanceof AbstractEntity) {
                            message.append(param.getClass().getName())
                                    .append("Entity id: ")
                                    .append(((AbstractEntity) param).getId())
                                    .append(" version: ")
                                    .append(((AbstractEntity) param).getVersion());
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
        } else if (result instanceof AbstractEntity) {
            message.append(result.getClass().getName())
                    .append(" Entity id: ")
                    .append(((AbstractEntity) result).getId())
                    .append(" version: ")
                    .append(((AbstractEntity) result).getVersion() + " ");
        } else if (result instanceof List) {
            for (Object obj : ((List<?>) result).toArray()) {
                message.append(obj.getClass().getName())
                        .append(" Entity id: ")
                        .append(((AbstractEntity) obj).getId())
                        .append(" version: ")
                        .append(((AbstractEntity) obj).getVersion() + " ");
            }
        }
        if (result instanceof Pair) {
            Pair pair = (Pair) result;
            if (pair.getLeft() instanceof List) {
                for (Object obj : ((List<?>) pair.getLeft()).toArray()) {
                    message.append(obj.getClass().getName())
                            .append(" Entity id: ")
                            .append(((AbstractEntity) obj).getId())
                            .append(" version: ")
                            .append(((AbstractEntity) obj).getVersion() + " ");
                }
            } else {
                message.append(pair.getLeft().getClass().getName())
                        .append(" Entity id: ")
                        .append(((AbstractEntity) pair.getLeft()).getId())
                        .append(" version: ")
                        .append(((AbstractEntity) pair.getLeft()).getVersion() + " ");
            }
            if (pair.getRight() instanceof List) {
                for (Object obj : ((List<?>) pair.getRight()).toArray()) {
                    message.append(obj.getClass().getName())
                            .append(" Entity id: ")
                            .append(((AbstractEntity) obj).getId())
                            .append(" version: ")
                            .append(((AbstractEntity) obj).getVersion() + " ");
                }
            } else {
                message.append(pair.getRight().getClass().getName())
                        .append(" Entity id: ")
                        .append(((AbstractEntity) pair.getRight()).getId())
                        .append(" version: ")
                        .append(((AbstractEntity) pair.getRight()).getVersion() + " ");
            }
        } else {
            message.append(result.toString()).append(" ");
        }

        logger.info(message.toString());

        return result;
}

    private String getInvokerId() {
        return sctx != null ?
                sctx.getCallerPrincipal().getName() : "UNKNOWN";
    }
}
