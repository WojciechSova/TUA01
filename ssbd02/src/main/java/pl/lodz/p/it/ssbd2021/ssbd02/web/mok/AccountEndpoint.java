package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody obsługujące żądania związane z modułem obsługi kont.
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
     * @return Lista kont zawierających zestaw ogólnych informacji o użytkownikach.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllAccountGenerals() {
        List<AccountGeneralDTO> accountGeneralDTOList = accountManager.getAllAccountsWithAccessLevels().stream()
                .map(AccountMapper::createAccountGeneralDTOFromEntities)
                .collect(Collectors.toList());
        return Response.ok()
                .entity(accountGeneralDTOList)
                .build();
    }

    /**
     * Metoda udostępniająca szczegółowe informacje dotyczące konta o podanym loginie.
     *
     * @param login Login wyszukiwanego konta
     * @return Szczegółowe informacje o koncie
     */
    @GET
    @Path("{login}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAccountWithLogin(@PathParam("login") String login) {
        AccountDetailsDTO account = AccountMapper
                .createAccountDetailsDTOFromEntities(accountManager.getAccountWithLogin(login));

        return Response.ok()
                .entity(account)
                .build();
    }

    /**
     * Metoda umożliwiająca użytkownikowi nieuprzywilejowanemu zarejestrowanie nowego konta w aplikacji.
     * @param accountDTO Obiekt typu {@link AccountDetailsDTO} przechowujący szczegóły nowego konta.
     * @return Kod 202 w przypadku poprawnej rejestracji.
     */
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
