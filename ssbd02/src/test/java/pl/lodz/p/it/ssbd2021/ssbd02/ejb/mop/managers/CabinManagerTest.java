package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.AccountMopFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;

import javax.ws.rs.WebApplicationException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class CabinManagerTest {

    @Mock
    CabinFacadeLocal cabinFacadeLocal;
    @Mock
    AccountMopFacadeLocal accountMopFacadeLocal;

    @InjectMocks
    CabinManager cabinManager;

    Cabin cabin1 = new Cabin();
    @Spy
    Cabin cabin2 = new Cabin();
    @Spy
    Account account = new Account();
    String login = "Franek";

    private List<Cabin> cabins;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        cabins = new ArrayList<>();
        cabins.add(cabin1);

        account.setLogin(login);
    }

    @Test
    void getCabinByNumber() {
        String cabinNumber = "123";
        when(cabinFacadeLocal.findByNumber(cabinNumber)).thenReturn(cabin1);
        assertEquals(cabin1, cabinManager.getCabinByNumber(cabinNumber));
        assertEquals(cabin1.hashCode(), cabinManager.getCabinByNumber(cabinNumber).hashCode());
    }

    @Test
    void createCabin() {
        doAnswer(invocationOnMock -> {
            cabins.add(cabin2);
            return null;
        }).when(cabinFacadeLocal).create(cabin2);
        when(accountMopFacadeLocal.findByLogin(login)).thenReturn(account);

        cabinManager.createCabin(cabin2, login);
        WebApplicationException exception = assertThrows(CommonExceptions.class, () -> cabinManager.createCabin(cabin2, "NieFranek"));

        assertAll(
                () -> assertEquals(2, cabins.size()),
                () -> assertEquals(cabin2.hashCode(), cabins.get(cabins.size() - 1).hashCode()),
                () -> assertEquals(account.getLogin(), cabin2.getCreatedBy().getLogin()),
                () -> assertEquals(0L, cabin2.getVersion()),
                () -> assertEquals(CommonExceptions.createNoResultException().getResponse().getStatus(), exception.getResponse().getStatus())
        );
    }

    @Test
    void updateCabin() {
        cabin1.setNumber("H666");
        cabin1.setVersion(1L);
        CabinType cabinType = new CabinType();
        cabinType.setCabinTypeName("Luksusowa");
        cabin1.setCabinType(cabinType);
        cabin1.setCapacity(123);
        when(cabinFacadeLocal.findByNumber("H666")).thenReturn(cabin1);

        assertEquals("H666", cabin1.getNumber());
        assertEquals(1L, cabin1.getVersion());
        assertEquals("Luksusowa", cabin1.getCabinType().getCabinTypeName());
        assertEquals(123, cabin1.getCapacity());

        CabinType cabinType1 = new CabinType();
        cabinType1.setCabinTypeName("Ciepła");
        Account modifiedBy = new Account();
        modifiedBy.setLogin("Autor");
        Cabin updateCabin = new Cabin();
        updateCabin.setNumber("H666");
        updateCabin.setVersion(2L);
        updateCabin.setCabinType(cabinType1);
        updateCabin.setCapacity(666);

        when(accountMopFacadeLocal.findByLogin("Autor")).thenReturn(modifiedBy);
        doAnswer(invocation -> {
            cabin1.setNumber("H666");
            cabin1.setVersion(2L);
            cabin1.setCabinType(cabinType1);
            cabin1.setCapacity(666);
            cabin1.setModificationDate(Timestamp.from(Instant.now()));
            cabin1.setModifiedBy(modifiedBy);
            return null;
        }).when(cabinFacadeLocal).edit(any());

        assertDoesNotThrow(() -> cabinManager.updateCabin(updateCabin, "Autor"));

        assertEquals("H666", cabin1.getNumber());
        assertEquals(2L, cabin1.getVersion());
        assertEquals("Ciepła", cabin1.getCabinType().getCabinTypeName());
        assertEquals(666, cabin1.getCapacity());
        assertTrue(cabin1.getModificationDate().compareTo(Timestamp.from(Instant.now())) < 10000);
        assertEquals(modifiedBy, cabin1.getModifiedBy());
    }
}
