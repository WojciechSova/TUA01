package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.PasswordDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOSignatureValidatorFilterBinding;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @RolesAllowed("ADMIN")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllAccountGenerals() {
        List<AccountGeneralDTO> accountGeneralDTOList = accountManager.getAllAccountsWithActiveAccessLevels().stream()
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
    @RolesAllowed({"ADMIN"})
    @Path("{login}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAccountWithLogin(@PathParam("login") String login) {
        AccountDetailsDTO account = AccountMapper
                .createAccountDetailsDTOFromEntities(accountManager.getAccountWithLogin(login));

        return Response.ok()
                .entity(account)
                .tag(DTOIdentitySignerVerifier.calculateDTOSignature(account))
                .build();
    }

    /**
     * Punkt dostępowy udostępniający informacje o koncie uwierzytelnionego uzytkownika.
     * Tylko użytkownicy uwierzytelnieni mogą skorzystać z tego punktu dostępowego.
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika.
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
    @PermitAll
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
     * @param login           Login użytkownika do którego dodany zostanie poziom dostępu.
     * @param accessLevel     Poziom dostępu, który ma zostać dodany do konta.
     * @return Kod 200 w przypadku poprawnego dodania dostępu.
     */
    @PUT
    @Path("addaccesslevel/{login}")
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.TEXT_PLAIN)
    public Response addAccessLevel(@Context SecurityContext securityContext, @PathParam("login") String login, String accessLevel) {
        accountManager.addAccessLevel(securityContext.getUserPrincipal().getName(), login, accessLevel);

        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiająca odebranie poziomu dostępu użytkownikowi o podanym loginie.
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika.
     * @param login           Login użytkownika któremu odebrany zostanie poziom dostępu.
     * @param accessLevel     Poziom dostępu, który ma zostać odebrany.
     * @return Kod 200 w przypadku poprawnego odebrania dostępu.
     */
    @PUT
    @Path("removeaccesslevel/{login}")
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.TEXT_PLAIN)
    public Response removeAccessLevel(@Context SecurityContext securityContext, @PathParam("login") String login, String accessLevel) {
        accountManager.removeAccessLevel(securityContext.getUserPrincipal().getName(), login, accessLevel);

        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiająca użytkownikowi aktualizowanie konta w aplikacji.
     *
     * @param accountDTO      Obiekt typu {@link AccountDetailsDTO} zawierający zaktualizowane pola konta.
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika.
     * @param eTag            ETag podawany w zawartości nagłówka "If-Match"
     * @return Kod 200 w przypadku poprawnej aktualizacji.
     */
    @PUT
    @RolesAllowed({"ADMIN"})
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @DTOSignatureValidatorFilterBinding
    public Response updateAccount(AccountDetailsDTO accountDTO, @Context SecurityContext securityContext,
                                  @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {
        if (accountDTO.getLogin() == null || accountDTO.getVersion() == null) {
            throw new WebApplicationException("Not all required fields were provided", 400);
        }
        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, accountDTO)) {
            throw new WebApplicationException("Not valid tag", 412);
        }
        accountManager.updateAccount(AccountMapper.createAccountFromAccountDetailsDTO(accountDTO),
                securityContext.getUserPrincipal().getName());
        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiająca zablokowanie konta użytkownika.
     *
     * @param login           Login blokowanego konta
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika.
     * @return Kod 200 w przypadku poprawnego zablokowania konta
     */
    @PUT
    @RolesAllowed({"ADMIN"})
    @Path("block/{login}")
    public Response blockAccount(@PathParam("login") String login, @Context SecurityContext securityContext) {
        accountManager.changeActivity(login, false, securityContext.getUserPrincipal().getName());

        return Response.ok().build();
    }

    /**
     * Metoda umożliwiająca uwierzytelnionemu użytkownikowi zmianę hasła do konta
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param passwordDTO     Obiekt typu {@link PasswordDTO} przchowujący aktualne oraz nowe hasło do konta
     * @return Kod 200 w przypadku poprawnej zmiany hasła
     */
    @PUT
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    @Path("password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(@Context SecurityContext securityContext, PasswordDTO passwordDTO) {
        if (passwordDTO.getOldPassword() == null || passwordDTO.getOldPassword().isBlank() ||
                passwordDTO.getNewPassword() == null || passwordDTO.getNewPassword().isBlank()) {
            throw new WebApplicationException("Required fields are missing", 400);
        }
        accountManager.changePassword(securityContext.getUserPrincipal().getName(), passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiająca odblokowanie konta użytkownika.
     *
     * @param login           Login odblokowywanego konta
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego odblokowania konta
     */
    @PUT
    @RolesAllowed({"ADMIN"})
    @Path("unblock/{login}")
    public Response unblockAccount(@PathParam("login") String login, @Context SecurityContext securityContext) {
        accountManager.changeActivity(login, true, securityContext.getUserPrincipal().getName());

        return Response.ok().build();
    }

    /**
     * Metoda umożliwiająca potwierdzenie nowo zarejestrowanego konta.
     *
     * @param url Kod służący do potwierdzenia konta
     * @return Kod 200 w przypadku poprawnego potwierdzenia konta, w przeciwnym razie kod 400
     */
    @PUT
    @PermitAll
    @Path("confirm/account/{url}")
    public Response confirmAccount(@PathParam("url") String url) {
        if (accountManager.confirmAccount(url)) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Metoda umożliwiająca wysłanie wiadomości z jednorazowym kodem url w celu zmiay adresu e-mail
     *
     * @param newEmailAddress Nowy adres e-mail
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego wysłania wiadomości o zmianie adresu e-mail
     */
    @POST
    @Path("changeemail")
    @RolesAllowed({"ADMIN", "CLIENT", "EMPLOYEE"})
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendChangeEmailAddressUrl(String newEmailAddress, @Context SecurityContext securityContext) {
        accountManager.sendChangeEmailAddressUrl(securityContext.getUserPrincipal().getName(), newEmailAddress);

        return Response.ok().build();
    }

    /**
     * Metoda umożliwiająca zmianę adresu e-mail przypisanego do konta
     *
     * @param url Kod służący do potwierdzenia zmiany adresu e-mail
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego potwierdzenia zmiany adresu e-mail, w przeciwnym razie kod 400
     */
    @PUT
    @RolesAllowed({"ADMIN", "CLIENT", "EMPLOYEE"})
    @Path("confirm/email/{url}")
    public Response changeEmailAddress(@PathParam("url") String url, @Context SecurityContext securityContext) {
        if (accountManager.changeEmailAddress(url, securityContext.getUserPrincipal().getName())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
