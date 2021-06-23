package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.AccountGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mok.PasswordDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.AccessLevelExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.OneTimeUrlExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOSignatureValidatorFilterBinding;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Password;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Klasa ziarna CDI o zasięgu żądania.
 * Zawiera metody obsługujące żądania związane z modułem obsługi kont.
 *
 * @author Daniel Łondka
 */
@RequestScoped
@Path("accounts")
@RolesAllowed({"DEFINITELY_NOT_A_REAL_ROLE"})
public class AccountEndpoint {

    private static final Logger logger = LogManager.getLogger();
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
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        List<AccountGeneralDTO> accountGeneralDTOList = null;
        do {
            try {
                accountGeneralDTOList = accountManager.getAllAccountsWithActiveAccessLevels().stream()
                        .map(AccountMapper::createAccountGeneralDTOFromEntities)
                        .collect(Collectors.toList());
                transactionRollBack = accountManager.isTransactionRolledBack();

            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);
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
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        AccountDetailsDTO account = null;
        do {
            try {
                account = AccountMapper
                        .createAccountDetailsDTOFromEntities(accountManager.getAccountWithLogin(login));
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);
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
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        AccountDetailsDTO account = null;
        do {
            try {
                account = AccountMapper.createAccountDetailsDTOFromEntities(
                        accountManager.getAccountWithLogin(securityContext.getUserPrincipal().getName()));
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .entity(account)
                .tag(DTOIdentitySignerVerifier.calculateDTOSignature(account))
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
    public Response createAccount(@Valid AccountDetailsDTO accountDTO) {
        if (accountDTO.getLogin() == null || accountDTO.getPassword() == null || accountDTO.getFirstName() == null
                || accountDTO.getLastName() == null || accountDTO.getEmail() == null
                || accountDTO.getLanguage() == null || accountDTO.getTimeZone() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.createAccount(AccountMapper.createAccountFromAccountDetailsDTO(accountDTO));
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

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
    public Response addAccessLevel(@Context SecurityContext securityContext,
                                   @PathParam("login") String login, @NotBlank String accessLevel) {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.addAccessLevel(securityContext.getUserPrincipal().getName(), login, accessLevel);
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

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
    public Response removeAccessLevel(@Context SecurityContext securityContext,
                                      @PathParam("login") String login, String accessLevel) {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.removeAccessLevel(securityContext.getUserPrincipal().getName(), login, accessLevel);
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

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
    public Response updateAccount(@Valid AccountDetailsDTO accountDTO, @Context SecurityContext securityContext,
                                  @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {
        if (accountDTO.getLogin() == null || accountDTO.getVersion() == null) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, accountDTO)) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.updateAccount(AccountMapper.createAccountFromAccountDetailsDTO(accountDTO),
                        securityContext.getUserPrincipal().getName());
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiająca użytkownikowi aktualizowanie swojego konta w aplikacji.
     *
     * @param accountDTO Obiekt typu {@link AccountDetailsDTO} zawierający zaktualizowane pola konta
     * @param eTag       ETag podawany w zawartości nagłówka "If-Match"
     * @return Kod 200 w przypadku poprawnej aktualizacji konta
     * Kod 400 w przypadku gdy przesyłane dane nie zawierają loginu lub wersji
     * Kod 412 w przypadku gdy eTag nie jest ważny lub próbujemy zmienić nie swoje konto
     */
    @PUT
    @RolesAllowed({"ADMIN", "EMPLOYEE", "CLIENT"})
    @Path("/profile/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @DTOSignatureValidatorFilterBinding
    public Response updateOwnAccount(@Valid AccountDetailsDTO accountDTO, @Context SecurityContext securityContext,
                                     @HeaderParam("If-Match") @NotNull @NotEmpty String eTag) {
        if (accountDTO.getLogin() == null || accountDTO.getVersion() == null) {
            throw CommonExceptions.createConstraintViolationException();
        }
        if (!DTOIdentitySignerVerifier.verifyDTOIntegrity(eTag, accountDTO)) {
            throw CommonExceptions.createPreconditionFailedException();
        }
        if (!accountDTO.getLogin().equals(securityContext.getUserPrincipal().getName())) {
            throw CommonExceptions.createPreconditionFailedException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.updateAccount(AccountMapper.createAccountFromAccountDetailsDTO(accountDTO), accountDTO.getLogin());
                transactionRollBack = accountManager.isTransactionRolledBack();

            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

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
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.changeActivity(login, false, securityContext.getUserPrincipal().getName());
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
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
    public Response changePassword(@Context SecurityContext securityContext, @Valid PasswordDTO passwordDTO) {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.changePassword(securityContext.getUserPrincipal().getName(),
                        new Password(passwordDTO.getOldPassword()), new Password(passwordDTO.getNewPassword()));
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

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
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.changeActivity(login, true, securityContext.getUserPrincipal().getName());
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
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
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        if (url.length() != 32) {
            throw OneTimeUrlExceptions.createBadRequestException(OneTimeUrlExceptions.ERROR_INVALID_ONE_TIME_URL);
        }
        do {
            try {
                accountManager.confirmAccount(url);
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiająca wysłanie wiadomości z jednorazowym kodem url w celu zmiay adresu e-mail
     *
     * @param newEmailAddress Nowy adres e-mail
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @return Kod 200 w przypadku poprawnego wysłania wiadomości o zmianie adresu e-mail
     */
    @POST
    @Path("profile/email")
    @RolesAllowed({"ADMIN", "CLIENT", "EMPLOYEE"})
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendChangeEmailAddressUrl(@NotBlank String newEmailAddress, @Context SecurityContext
            securityContext) {
        if (!EmailAddressValidator.isValid(newEmailAddress)) {
            throw CommonExceptions.createConstraintViolationException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.sendChangeEmailAddressUrl(securityContext.getUserPrincipal().getName(), newEmailAddress,
                        securityContext.getUserPrincipal().getName());
                transactionRollBack = accountManager.isTransactionRolledBack();

            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda umożliwiająca wysłanie wiadomości z jednorazowym kodem url w celu zmiany adresu e-mail użytkownika o podanym loginie.
     *
     * @param newEmailAddress Nowy adres e-mail.
     * @param login           Login użytkownika, któremy ma zostać zmieniony adres e-mail.
     * @return Kod 200 w przypadku poprawnego wysłania wiadomości o zmianie adresu e-mail
     */
    @POST
    @Path("email/{login}")
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendChangeEmailAddressUrl(@NotBlank String newEmailAddress, @PathParam("login") String login,
                                              @Context SecurityContext securityContext) {
        if (!EmailAddressValidator.isValid(newEmailAddress)) {
            throw CommonExceptions.createConstraintViolationException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.sendChangeEmailAddressUrl(login, newEmailAddress, securityContext.getUserPrincipal().getName());
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok().build();
    }

    /**
     * Metoda umożliwiająca zmianę adresu e-mail przypisanego do konta
     *
     * @param url Kod służący do potwierdzenia zmiany adresu e-mail
     * @return Kod 200 w przypadku poprawnego potwierdzenia zmiany adresu e-mail, w przeciwnym razie kod 400
     */
    @PUT
    @PermitAll
    @Path("confirm/email/{url}")
    public Response changeEmailAddress(@PathParam("url") String url) {
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        if (url.length() != 32) {
            throw OneTimeUrlExceptions.createBadRequestException(OneTimeUrlExceptions.ERROR_INVALID_ONE_TIME_URL);
        }
        do {
            try {
                accountManager.changeEmailAddress(url);
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                if (generalException.getMessage().equals(CommonExceptions.createOptimisticLockException().getMessage())) {
                    transactionRollBack = true;
                    if (transactionRetryCounter < 2) {
                        throw generalException;
                    }
                } else {
                    throw generalException;
                }
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda obsługująca żądanie resetowania hasła.
     *
     * @param email Email użytkownika, którego hasło ma zostać zresetowane
     * @return Kod 200 w przypadku poprawnego formatu adresu email, w przeciwnym razie 400.
     * Aplikacja nie powiadamia użytkownika czy podany email znajduje się w bazie danych.
     */
    @POST
    @PermitAll
    @Path("reset/password")
    public Response sendPasswordResetAddressUrl(@NotBlank String email, @Context SecurityContext securityContext) {
        if (email == null || "".equals(email.trim())) {
            throw CommonExceptions.createConstraintViolationException();
        }
        if (!EmailAddressValidator.isValid(email)) {
            throw CommonExceptions.createConstraintViolationException();
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                if (securityContext.getUserPrincipal() != null) {
                    accountManager.sendPasswordResetAddressUrl(email, securityContext.getUserPrincipal().getName());
                } else {
                    accountManager.sendPasswordResetAddressUrl(email, null);
                }
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda zmieniająca hasło użytkownika na podstawie dostarczonego wcześniej jednorazowego adresu URL.
     *
     * @param url         Jednorazowy adres url potwierdzający możliwość zmiany hasła.
     * @param newPassword Nowe hasło użytkownika.
     * @return Kod 200 w przypadku poprawnie skonstruowanego żądania.
     * Kod 400 w przypadku nieprawidłowej długości url.
     */
    @PUT
    @PermitAll
    @Path("reset/password/{url}")
    public Response resetPassword(@PathParam("url") String url, @NotBlank String newPassword) {
        if (url.length() != 32) {
            throw OneTimeUrlExceptions.createBadRequestException(OneTimeUrlExceptions.ERROR_INVALID_ONE_TIME_URL);
        }
        if (newPassword.length() < 8) {
            throw CommonExceptions.createConstraintViolationException();
        }

        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                accountManager.resetPassword(url, new Password(newPassword));
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda zmianiający aktualny poziom dostępu użytkownika.
     *
     * @param securityContext Interfejs wstrzykiwany w celu pozyskania tożsamości aktualnie uwierzytelnionego użytkownika
     * @param accessLevel     Poziom dostępu, który ma zostać zmieniony
     * @return Kod 200 w przypadku poprawnej zmiany poziomu dostępu. Kod 400 w przypadku podania nieistniejącego
     * poziomu dostępu
     */
    @POST
    @RolesAllowed({"ADMIN", "CLIENT", "EMPLOYEE"})
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("change/accesslevel")
    public Response informAboutAccessLevelChange(@Context SecurityContext securityContext, @NotBlank String
            accessLevel) {
        if (!List.of("ADMIN", "EMPLOYEE", "CLIENT").contains(accessLevel)) {
            throw AccessLevelExceptions.createBadRequestException(AccessLevelExceptions.ERROR_NO_ACCESS_LEVEL);
        }
        int transactionRetryCounter = getTransactionRepetitionCounter();
        boolean transactionRollBack = false;
        do {
            try {
                logger.info("The user with login {} changed the access level to {}",
                        securityContext.getUserPrincipal().getName(), accessLevel);
                transactionRollBack = accountManager.isTransactionRolledBack();
            } catch (GeneralException generalException) {
                throw generalException;
            } catch (EJBAccessException | AccessLocalException accessExcept) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createForbiddenException();
                }
            } catch (EJBException ejbException) {
                if (transactionRetryCounter < 2) {
                    throw CommonExceptions.createUnknownException();
                }
            } catch (Exception e) {
                throw CommonExceptions.createUnknownException();
            }
        } while (transactionRollBack && --transactionRetryCounter > 0);

        return Response.ok()
                .build();
    }

    /**
     * Metoda pobierająca z właściwości współczynnik określający ilość powtórzeń transakcji.
     *
     * @return Współczynnik powtórzeń transakcji
     */
    private int getTransactionRepetitionCounter() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("system.properties")) {
            prop.load(input);
            return Integer.parseInt(prop.getProperty("system.transaction.repetition"));
        } catch (IOException | NullPointerException | NumberFormatException e) {
            logger.warn(e);
            return 3;
        }
    }
}
