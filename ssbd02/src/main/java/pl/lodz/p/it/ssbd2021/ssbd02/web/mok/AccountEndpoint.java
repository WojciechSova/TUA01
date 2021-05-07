package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.sql.Time;
import java.util.Date;
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
     * Punkt dostępowy udostępniający informacje o koncie uwierzytelnionego uzytkownika.
     * Tylko użytkownicy uwierzytelnieni mogą skorzystać z tego punktu dostępowego.
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkwnika.
     * @return Szczegóły konta aktualnie uwierzytelnionego użytkownika.
     */
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

    /**
     * Metoda umożliwiająca użytkownikowi nieuprzywilejowanemu zarejestrowanie nowego konta w aplikacji.
     *
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

    /**
     * Metoda umożliwiająca dodanie poziomu dostępu użytkownikowi o podanym loginie.
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika.
     * @param login Login użytkownika do którego dodany zostanie poziom dostępu.
     * @param accessLevel Poziom dostępu, który ma zostać dodany do konta.
     * @return Kod 200 w przypadku poprawnego dodania dostępu.
     */
    @PUT
    @Path("addaccesslevel/{login}")
    @RolesAllowed({"ADMIN"})
    public Response addAccessLevel(@Context SecurityContext securityContext, @PathParam("login") String login, String accessLevel) {
        accountManager.addAccessLevel(securityContext.getUserPrincipal().getName(), login, accessLevel);

        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiająca odebranie poziomu dostępu użytkownikowi o podanym loginie.
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika.
     * @param login Login użytkownika któremu odebrany zostanie poziom dostępu.
     * @param accessLevel Poziom dostępu, który ma zostać odebrany.
     * @return Kod 200 w przypadku poprawnego odebrania dostępu.
     */
    @PUT
    @Path("removeaccesslevel/{login}")
    @RolesAllowed({"ADMIN"})
    public Response removeAccessLevel(@Context SecurityContext securityContext, @PathParam("login") String login, String accessLevel) {
        accountManager.removeAccessLevel(securityContext.getUserPrincipal().getName(), login, accessLevel);

        return Response.ok()
                .build();
    }
}
