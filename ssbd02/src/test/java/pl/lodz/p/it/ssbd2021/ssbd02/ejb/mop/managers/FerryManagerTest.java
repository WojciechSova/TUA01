package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;

import javax.ws.rs.WebApplicationException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FerryManagerTest {

    private final String ferryName1 = "ferry1";
    private final String accountLogin1 = "login1";
    private final String login = "login";
    private final List<Cabin> cabins = new ArrayList<>();

    @Mock
    FerryFacadeLocal ferryFacadeLocal;

    @Mock
    CabinFacadeLocal cabinFacadeLocal;

    @Mock
    AccountMopFacadeLocal accountMopFacadeLocal;

    @Mock
    CruiseFacadeLocal cruiseFacadeLocal;

    @Mock
    BookingFacadeLocal bookingFacadeLocal;


    @InjectMocks
    FerryManager ferryManager;

    @Captor
    private ArgumentCaptor<Ferry> ferryCaptor;

    @Spy
    Ferry ferry1 = new Ferry();
    @Spy
    Ferry ferry2 = new Ferry();
    @Spy
    Ferry ferry3 = new Ferry();
    @Spy
    Account account = new Account();

    private List<Ferry> ferries;

    private Cruise cruise = new Cruise();

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        ferries = new ArrayList<>();
        ferries.addAll(Arrays.asList(ferry1, ferry2));
        ferry1.setOnDeckCapacity(12);
        cruise.setFerry(ferry1);
    }

    @Test
    void getAllFerriesTest() {
        when(ferryFacadeLocal.findAll()).thenReturn(ferries);

        assertEquals(ferries.hashCode(), ferryManager.getAllFerries().hashCode());
    }

    @Test
    void getAllFerriesExceptionTest() {
        doAnswer(invocationOnMock -> {
            throw CommonExceptions.createNoResultException();
        }).when(ferryFacadeLocal).findAll();
        WebApplicationException exception = assertThrows(CommonExceptions.class, () -> ferryManager.getAllFerries());

        assertAll(
                () -> assertEquals(CommonExceptions.createNoResultException().getResponse().getStatus(), exception.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.createNoResultException().getMessage(), exception.getMessage())
        );
    }

    @Test
    void getFerryByNameTest() {
        when(ferryFacadeLocal.findByName(ferryName1)).thenReturn(ferry1);
        assertDoesNotThrow(() -> ferryManager.getFerryByName(ferryName1));
        assertEquals(ferry1, ferryManager.getFerryByName(ferryName1));
        assertEquals(ferry1.hashCode(), ferryManager.getFerryByName(ferryName1).hashCode());
        verify(ferryFacadeLocal, times(3)).findByName(ferryName1);
    }

    @Test
    void getFerryByNameExceptionTest() {
        doAnswer(invocationOnMock -> {
            throw CommonExceptions.createNoResultException();
        }).when(ferryFacadeLocal).findByName(ferryName1);

        assertThrows(CommonExceptions.class, () -> ferryManager.getFerryByName(ferryName1));
        verify(ferryFacadeLocal).findByName(ferryName1);
    }

    @Test
    void getFerryAndCabinsByFerryNameTest() {
        when(ferryFacadeLocal.findByName(ferryName1)).thenReturn(ferry1);
        when(cabinFacadeLocal.findAllByFerry(ferry1)).thenReturn(cabins);
        assertEquals(Pair.of(ferry1, cabins), ferryManager.getFerryAndCabinsByFerryName(ferryName1));
        assertEquals(ferry1, ferryManager.getFerryAndCabinsByFerryName(ferryName1).getLeft());
        assertEquals(ferry1, ferryManager.getFerryAndCabinsByFerryName(ferryName1).getKey());
        assertEquals(cabins, ferryManager.getFerryAndCabinsByFerryName(ferryName1).getRight());
        assertEquals(cabins, ferryManager.getFerryAndCabinsByFerryName(ferryName1).getValue());
        verify(ferryFacadeLocal, times(5)).findByName(ferryName1);
        verify(cabinFacadeLocal, times(5)).findAllByFerry(ferry1);
    }

    @Test
    void createFerry() {
        doAnswer(invocationOnMock -> {
            ferries.add(ferry3);
            return null;
        }).when(ferryFacadeLocal).create(ferry3);

        assertEquals(2, ferries.size());
        assertDoesNotThrow(() -> ferryManager.createFerry(login, ferry3));
        assertEquals(3, ferries.size());
        assertEquals(ferry3.hashCode(), ferries.get(2).hashCode());
        verify(ferryFacadeLocal).create(ferry3);
    }

    @Test
    void updateFerry() {
        Ferry ferry = new Ferry();

        Timestamp timestamp = Timestamp.from(Instant.now());
        ferry.setName(ferryName1);
        ferry.setVersion(1L);
        ferry.setVehicleCapacity(12);
        ferry.setOnDeckCapacity(13);
        ferry.setModifiedBy(account);
        ferry.setModificationDate(timestamp);

        when(ferryFacadeLocal.findByName(ferryName1)).thenReturn(ferry);
        when(accountMopFacadeLocal.findByLogin(accountLogin1)).thenReturn(account);
        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(any())).thenReturn(List.of());
        when(cabinFacadeLocal.findCabinsOnCruise(any())).thenReturn(List.of());
        when(bookingFacadeLocal.getSumNumberOfPeopleByCruise(any())).thenReturn(10L);
        when(cruiseFacadeLocal.findByNumber(any())).thenReturn(cruise);

        ferryManager.updateFerry(ferry, accountLogin1);
        verify(ferryFacadeLocal).edit(ferryCaptor.capture());
        Ferry capturedFerry = ferryCaptor.getValue();

        assertEquals(1L, capturedFerry.getVersion());
        assertEquals(12, capturedFerry.getVehicleCapacity());
        assertEquals(13, capturedFerry.getOnDeckCapacity());
        assertEquals(account, capturedFerry.getModifiedBy());
        assertTrue(timestamp.before(capturedFerry.getModificationDate()));
    }

    @Test
    void removeFerry() {
        when(ferryFacadeLocal.findByName("ferry")).thenReturn(ferry1);
        ferryManager.removeFerry("ferry", "login");

        verify(ferryFacadeLocal).remove(ferry1);
    }
}
