package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(AccountDetailsDTO accountDTO) {
        if (accountDTO.getPassword() == null || accountDTO.getEmail() == null || accountDTO.getLogin() == null) {
            throw new WebApplicationException("Not all required fields were provided", 400);
        }
        accountManager.createAccount(AccountMapper.createAccountFromAccountDetailsDTO(accountDTO));
        return Response.accepted()
                .build();
    }
}
