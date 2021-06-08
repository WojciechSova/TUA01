package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.AccountMopFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.SeaportFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SeaportManagerTest {

    @Spy
    private final Seaport s1 = new Seaport();
    @Spy
    private final Seaport s2 = new Seaport();
    @Spy
    private final Seaport s3 = new Seaport();
    @Mock
    private SeaportFacadeLocal seaportFacadeLocal;
    @Mock
    private AccountMopFacadeLocal accountMopFacadeLocal;
    @InjectMocks
    private SeaportManager seaportManager;
    private List<Seaport> seaports;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        s1.setCity("Warszawa");
        s2.setCity("Ciechocinek");
        s3.setCity("Pabianice");
        seaports = new ArrayList<>();
        seaports.add(s1);
        seaports.add(s2);

        when(seaportFacadeLocal.findAll()).thenReturn(seaports);
    }

    @Test
    void getAllSeaports() {
        List<Seaport> testedSeaports = seaportManager.getAllSeaports();

        assertEquals(2, testedSeaports.size());
        assertEquals("Warszawa", testedSeaports.get(0).getCity());
        assertEquals("Ciechocinek", testedSeaports.get(1).getCity());

        verify(seaportFacadeLocal).findAll();
    }

    @Test
    void getSeaportByCode() {
        when(seaportFacadeLocal.findByCode("CODE")).thenReturn(s1);
        when(seaportFacadeLocal.findByCode(null)).thenReturn(null);

        Seaport seaport = seaportManager.getSeaportByCode("CODE");
        assertEquals(s1, seaport);

        GeneralException exception = Assertions.assertThrows(CommonExceptions.class,
                () -> seaportManager.getSeaportByCode(null));

        Assertions.assertEquals(410, exception.getResponse().getStatus());
        Assertions.assertEquals(CommonExceptions.ERROR_NO_RESULT, exception.getResponse().getEntity());
    }

    @Test
    void createSeaport() {
        doAnswer(invocationOnMock -> {
            seaports.add(s3);
            return null;
        }).when(seaportFacadeLocal).create(s3);

        seaportManager.createSeaport(s3);

        assertEquals(3, seaports.size());
        assertEquals(s3.hashCode(), seaports.get(seaports.size() - 1).hashCode());
    }

    @Test
    void updateSeaport() {
        Account account = new Account();
        account.setLogin("login");

        Seaport oldSeaport = new Seaport();
        oldSeaport.setCity("Warszawa");
        oldSeaport.setVersion(0L);
        oldSeaport.setCode("CODE");

        Seaport newSeaport = new Seaport();
        newSeaport.setCity("Lublin");
        newSeaport.setVersion(0L);
        oldSeaport.setCode("CODE");

        when(seaportFacadeLocal.findByCode("CODE")).thenReturn(oldSeaport);
        when(accountMopFacadeLocal.findByLogin("login")).thenReturn(account);

        doAnswer(invocation -> {
            oldSeaport.setCity("Lublin");
            oldSeaport.setModifiedBy(account);
            oldSeaport.setModificationDate(Timestamp.from(Instant.now()));
            oldSeaport.setVersion(1L);
            return null;
        }).when(seaportFacadeLocal).edit(any());

        seaportManager.updateSeaport(oldSeaport, "login");

        assertEquals("Lublin", oldSeaport.getCity());
        assertEquals(account, oldSeaport.getModifiedBy());
        assertEquals(1L, oldSeaport.getVersion());
        assertTrue(oldSeaport.getModificationDate().compareTo(Timestamp.from(Instant.now())) < 0);
    }
}
