package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class AccountEndpointTest {

    private final List<Account> accounts = new ArrayList<>();
    @Mock
    private AccountManagerLocal accountManager;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserPrincipal userPrincipal;
    @InjectMocks
    private AccountEndpoint accountEndpoint;
    private Account account;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        account = createAccount();
    }

    private Account createAccount() {
        Account acc = new Account();
        acc.setLogin("ExampleLogin");
        acc.setPassword("P@ssword");
        acc.setActive(true);
        acc.setConfirmed(true);
        acc.setFirstName("Annabelle");
        acc.setLastName("Washington");
        acc.setEmail("example@example.com");
        acc.setPhoneNumber("48123456788");
        acc.setLanguage("PL");
        acc.setTimeZone("PL");
        acc.setModificationDate(Timestamp.from(Instant.now()));
        acc.setModifiedBy(null);
        acc.setCreationDate(Timestamp.valueOf("2020-03-21 11:21:15"));
        acc.setLastKnownGoodLogin(Timestamp.valueOf("2020-03-25 11:21:15"));
        acc.setLastKnownGoodLoginIp("111.111.111.111");
        acc.setLastKnownBadLogin(Timestamp.valueOf("2020-03-26 11:21:15"));
        acc.setLastKnownBadLoginIp("222.222.222.222");
        acc.setNumberOfBadLogins(2);
        return acc;
    }

    @Test
    void getProfile() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("ExampleLogin");
        when(accountManager.getAccountWithLogin("ExampleLogin"))
                .thenReturn(Pair.of(account, Collections.emptyList()));

        Response response = accountEndpoint.getProfile(securityContext);

        assertEquals(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())),
                response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void createAccountTest() {
        doAnswer(invocationOnMock -> {
            accounts.add(account);
            return null;
        }).when(accountManager).createAccount(any());

        assertEquals(0, accounts.size());

        Response response = accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())));
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals(1, accounts.size());
    }

    @Test
    void createAccountExceptionTest() {
        Account accountWithoutLogin = createAccount();
        accountWithoutLogin.setLogin(null);
        Account accountWithoutPassword = createAccount();
        accountWithoutPassword.setPassword(null);
        Account accountWithoutEmail = createAccount();
        accountWithoutEmail.setEmail(null);

        WebApplicationException loginException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutLogin, Collections.emptyList()))));
        WebApplicationException passwordException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutPassword, Collections.emptyList()))));
        WebApplicationException emailException = assertThrows(WebApplicationException.class,
                () -> accountEndpoint.createAccount(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(accountWithoutEmail, Collections.emptyList()))));

        assertEquals(400, loginException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", loginException.getMessage());

        assertEquals(400, passwordException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", passwordException.getMessage());

        assertEquals(400, emailException.getResponse().getStatus());
        assertEquals("Not all required fields were provided", emailException.getMessage());
    }
}
