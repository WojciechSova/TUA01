package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccessLevelFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.utils.interfaces.EmailSenderLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SystemManagerTest {

    @Spy
    private final Account a1 = new Account();
    @Spy
    private final Account a2 = new Account();
    @Spy
    private final Account a3 = new Account();
    @Spy
    private final AccessLevel al1 = new AccessLevel();
    @Spy
    private final AccessLevel al2 = new AccessLevel();
    @Spy
    private final AccessLevel al3 = new AccessLevel();
    private final String name1 = "Artur";
    private final String mail1 = "ferrytales@protonmail.com";
    private final String name2 = "Artur2";
    private final String mail2 = "ferrytales@protonmail.com";
    private final String name3 = "Artur3";
    private final String mail3 = "ferrytales@protonmail.com";
    @Mock
    private AccountFacadeLocal accountFacadeLocal;
    @Mock
    private AccessLevelFacadeLocal accessLevelFacadeLocal;
    @Mock
    private EmailSenderLocal emailSender;
    @InjectMocks
    private SystemManager systemManager;
    private List<AccessLevel> accessLevels;
    private List<Account> accounts;


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        doNothing().when(emailSender).sendRegistrationEmail(anyString(), anyString(), anyString());
        doNothing().when(emailSender).sendChangedActivityEmail(anyString(), anyString(), anyBoolean());
        doNothing().when(emailSender).sendModificationEmail(anyString(), anyString());
        doNothing().when(emailSender).sendAddAccessLevelEmail(anyString(), anyString(), anyString());
        doNothing().when(emailSender).sendRemoveAccessLevelEmail(anyString(), anyString(), anyString());
        doNothing().when(emailSender).sendRemovalEmail(anyString(), anyString());
        doNothing().when(emailSender).sendAdminAuthenticationEmail(anyString(), anyString(), anyString());

        accessLevels = new ArrayList<>();
        accounts = new ArrayList<>();

        al1.setAccount(a1);
        al2.setAccount(a2);
        al3.setAccount(a2);

        accessLevels.addAll(List.of(al1, al2, al3));
        accounts.addAll(List.of(a1, a2, a3));
    }

    @Test
    void removeUnconfirmedAccountsTest() {
        when(a1.getCreationDate())
                .thenReturn(Timestamp.from(Instant.now().minus(Duration.ofHours(24).plus(Duration.ofSeconds(1)))));
        when(a2.getCreationDate())
                .thenReturn(Timestamp.from(Instant.now().minus(Duration.ofHours(24).plus(Duration.ofMinutes(5)))));
        when(a3.getCreationDate())
                .thenReturn(Timestamp.from(Instant.now().minus(Duration.ofHours(23).plus(Duration.ofMinutes(59)))));

        when(a1.getFirstName()).thenReturn(name1);
        when(a1.getEmail()).thenReturn(mail1);
        when(a2.getFirstName()).thenReturn(name2);
        when(a2.getEmail()).thenReturn(mail2);
        when(a3.getFirstName()).thenReturn(name3);
        when(a3.getEmail()).thenReturn(mail3);

        when(accountFacadeLocal.findByConfirmed(false)).thenReturn(accounts);
        when(accessLevelFacadeLocal.findAllByAccount(a1)).thenReturn(List.of(al1, al2));

        doAnswer(invocationOnMock -> {
            accessLevels.remove(al1);
            return null;
        }).when(accessLevelFacadeLocal).remove(al1);
        doAnswer(invocationOnMock -> {
            accessLevels.remove(al2);
            return null;
        }).when(accessLevelFacadeLocal).remove(al2);
        doAnswer(invocationOnMock -> {
            accessLevels.remove(al3);
            return null;
        }).when(accessLevelFacadeLocal).remove(al3);

        doAnswer(invocationOnMock -> {
            accounts.remove(a1);
            return null;
        }).when(accountFacadeLocal).remove(a1);
        doAnswer(invocationOnMock -> {
            accounts.remove(a2);
            return null;
        }).when(accountFacadeLocal).remove(a2);
        doAnswer(invocationOnMock -> {
            accounts.remove(a3);
            return null;
        }).when(accountFacadeLocal).remove(a3);

        systemManager.removeUnconfirmedAccounts();

        assertEquals(1, accounts.size());
        assertEquals(1, accessLevels.size());

        assertEquals(a3, accounts.get(0));
        assertEquals(al3, accessLevels.get(0));
    }
}
