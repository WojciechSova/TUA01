package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.facades.interfaces.AccountFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class CabinManagerTest {

    @Mock
    CabinFacadeLocal cabinFacadeLocal;
    @Mock
    AccountFacadeLocal accountFacadeLocal;

    @InjectMocks
    CabinManager cabinManager;

    @Spy
    Cabin cabin1 = new Cabin();

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void getCabinByNumber() {
        String cabinNumber = "123";
        when(cabinFacadeLocal.findByNumber(cabinNumber)).thenReturn(cabin1);
        assertEquals(cabin1, cabinManager.getCabinByNumber(cabinNumber));
        assertEquals(cabin1.hashCode(), cabinManager.getCabinByNumber(cabinNumber).hashCode());
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

        when(accountFacadeLocal.findByLogin("Autor")).thenReturn(modifiedBy);
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
