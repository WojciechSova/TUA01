package pl.lodz.p.it.ssbd2021.ssbd02.web.mok;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.AccountMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.nio.file.attribute.UserPrincipal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AccountEndpointTest {

    @Mock
    private AccountManagerLocal accountManager;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserPrincipal userPrincipal;

    @InjectMocks
    private AccountEndpoint accountEndpoint;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
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
        Account account = createAccount();

        Mockito.when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        Mockito.when(userPrincipal.getName()).thenReturn("ExampleLogin");
        Mockito.when(accountManager.getAccountWithLogin("ExampleLogin"))
                .thenReturn(Pair.of(account, Collections.emptyList()));

        Response response = accountEndpoint.getProfile(securityContext);

        assertEquals(AccountMapper.createAccountDetailsDTOFromEntities(Pair.of(account, Collections.emptyList())),
                response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
