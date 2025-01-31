package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.AccountMopFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.FerryFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CabinExceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CabinManagerTest {

    @Mock
    CabinFacadeLocal cabinFacadeLocal;
    @Mock
    AccountMopFacadeLocal accountMopFacadeLocal;
    @Mock
    FerryFacadeLocal ferryFacadeLocal;
    @Mock
    CruiseFacadeLocal cruiseFacadeLocal;


    @InjectMocks
    CabinManager cabinManager;

    Cabin cabin1 = new Cabin();
    Cabin cabin3 = new Cabin();
    Cabin cabin4 = new Cabin();
    @Spy
    Cabin cabin2 = new Cabin();
    @Spy
    Account account = new Account();
    @Spy
    Ferry ferry = new Ferry();
    String login = "Franek";
    String ferryName = "Perl";

    Cruise cruise = new Cruise();


    private List<Cabin> cabins;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        cabin1.setNumber("A123");
        cabin3.setNumber("B124");
        cabin4.setNumber("H666");
        cabins = new ArrayList<>();
        cabins.add(cabin1);

        account.setLogin(login);
        ferry.setName(ferryName);
        cruise.setNumber("ABCDEF000000");

    }

    @Test
    void getCabinByFerryAndNumber() {
        String cabinNumber = "123";
        when(ferryFacadeLocal.findByName(ferryName)).thenReturn(ferry);
        when(cabinFacadeLocal.findByFerryAndNumber(ferry, cabinNumber)).thenReturn(cabin1);
        assertEquals(cabin1, cabinManager.getCabinByFerryAndNumber(ferryName, cabinNumber));
        assertEquals(cabin1.hashCode(), cabinManager.getCabinByFerryAndNumber(ferryName, cabinNumber).hashCode());
    }

    @Test
    void createCabin() {
        doAnswer(invocationOnMock -> {
            cabins.add(cabin2);
            return null;
        }).when(cabinFacadeLocal).create(cabin2);
        doAnswer(invocationOnMock -> {
            throw CommonExceptions.createNoResultException();
        }).when(accountMopFacadeLocal).findByLogin("NieFranek");

        when(accountMopFacadeLocal.findByLogin(login)).thenReturn(account);
        when(ferryFacadeLocal.findByName(ferryName)).thenReturn(ferry);

        cabinManager.createCabin(cabin2, login, ferryName);
        WebApplicationException exception = assertThrows(CommonExceptions.class, () -> cabinManager.createCabin(cabin2, "NieFranek", ferryName));

        assertAll(
                () -> assertEquals(2, cabins.size()),
                () -> assertEquals(cabin2.hashCode(), cabins.get(cabins.size() - 1).hashCode()),
                () -> assertEquals(account.getLogin(), cabin2.getCreatedBy().getLogin()),
                () -> assertEquals(0L, cabin2.getVersion()),
                () -> assertEquals(ferry.getName(), cabin2.getFerry().getName()),
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
        when(cabinFacadeLocal.findByFerryAndNumber(any(), any())).thenReturn(cabin1);

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

        assertDoesNotThrow(() -> cabinManager.updateCabin(updateCabin, "Autor", "Name"));

        assertEquals("H666", cabin1.getNumber());
        assertEquals(2L, cabin1.getVersion());
        assertEquals("Ciepła", cabin1.getCabinType().getCabinTypeName());
        assertEquals(666, cabin1.getCapacity());
        assertTrue(cabin1.getModificationDate().compareTo(Timestamp.from(Instant.now())) < 10000);
        assertEquals(modifiedBy, cabin1.getModifiedBy());
    }

    @Test
    void getFreeCabinsOnCruise() {
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);

        when(cabinFacadeLocal.findCabinsOnCruise(cruise)).thenReturn(List.of(cabin1, cabin3, cabin4));
        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(cruise)).thenReturn(List.of(cabin4));

        assertDoesNotThrow(() -> cabinManager.getFreeCabinsOnCruise(cruise.getNumber()));
        List<Cabin> freeCabins = cabinManager.getFreeCabinsOnCruise(cruise.getNumber());

        assertEquals(2, freeCabins.size());
        assertTrue(freeCabins.contains(cabin1));
        assertTrue(freeCabins.contains(cabin3));
        assertFalse(freeCabins.contains(cabin4));

    }

    @Test
    void removeCabin() {
        when(cabinFacadeLocal.findByNumber(cabin1.getNumber())).thenReturn(cabin1);
        cabinManager.removeCabin(cabin1.getNumber(), login);
        verify(cabinFacadeLocal).remove(cabin1);
    }

    @Test
    void removeCabinException() {
        when(cabinFacadeLocal.findByNumber(cabin1.getNumber())).thenReturn(cabin1);
        doAnswer(invocationOnMock -> {
            throw CommonExceptions.createConstraintViolationException();
        }).when(cabinFacadeLocal).remove(cabin1);

        CabinExceptions exception = assertThrows(CabinExceptions.class, () -> cabinManager.removeCabin(cabin1.getNumber(), login));

        assertEquals(Response.Status.CONFLICT.getStatusCode(), exception.getResponse().getStatus());
        assertEquals(CabinExceptions.ERROR_CABIN_USED_BY_BOOKING, exception.getResponse().getEntity());
    }
}
