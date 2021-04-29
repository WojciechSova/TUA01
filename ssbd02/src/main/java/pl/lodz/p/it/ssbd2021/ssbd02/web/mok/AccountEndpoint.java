package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody zawierające zapytania związane z modułem obsługi kont.
 *
 * @author Daniel Łondka
 */
@RequestScoped
@Path("accounts")
public class AccountEndpoint {

    @Inject
    private AccountManagerLocal accountManager;

    /**
     * Metoda udostępniająca ogólne informacje o kontach aplikacji.
     *
     * @return Lista ogólnych informacji o kontach aplikacji.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllAccountGenerals() {
        List<AccountGeneralDTO> accountGeneralDTOList = accountManager.getAllAccountsWithAccessLevels().stream()
                .map(AccountMapper::createAccountGeneralDTOFromEntities)
                .collect(Collectors.toList());
        return Response.accepted(accountGeneralDTOList)
                .entity(accountGeneralDTOList)
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    @Path("/profile")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getProfile(@Context SecurityContext securityContext) {
        AccountDetailsDTO account = AccountMapper.createAccountDetailsDTOFromEntities(
                accountManager.getAccountWithLogin(securityContext.getUserPrincipal().getName())
        );
        return Response.ok(account)
                .build();
    }
}
