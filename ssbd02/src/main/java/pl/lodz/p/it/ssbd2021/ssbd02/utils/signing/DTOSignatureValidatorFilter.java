package pl.lodz.p.it.ssbd2021.ssbd02.utils.signing;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Klasa implementująca mechanizm sprawdzania poprawności nagłówka podpisu.
 *
 * @author Karolina Kowalczyk
 */
@Provider
@DTOSignatureValidatorFilterBinding
public class DTOSignatureValidatorFilter implements ContainerRequestFilter {

    /**
     * Metoda sprawdzająca obecność i poprawność podpisu przekazanego w żądaniu.
     *
     * @param requestContext Obiekt typu {@link ContainerRequestContext}.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String header = requestContext.getHeaderString("If-Match");
        if (header == null || header.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        } else if (!DTOIdentitySignerVerifier.validateDTOSignature(header)) {
            requestContext.abortWith(Response.status(Response.Status.PRECONDITION_FAILED).build());
        }
    }

}
