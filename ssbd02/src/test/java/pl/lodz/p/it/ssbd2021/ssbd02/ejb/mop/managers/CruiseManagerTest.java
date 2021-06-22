package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;

import javax.ws.rs.WebApplicationException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CruiseManagerTest {

    @Spy
    private final Cruise cruise2 = new Cruise();
    @Spy
    private final Cruise cruise3 = new Cruise();
    @Mock
    private CruiseFacadeLocal cruiseFacadeLocal;
    @Mock
    private FerryFacadeLocal ferryFacadeLocal;
    @Mock
    private RouteFacadeLocal routeFacadeLocal;
    @Mock
    private AccountMopFacadeLocal accountMopFacadeLocal;
    @Mock
    private CabinFacadeLocal cabinFacadeLocal;
    @Mock
    private BookingFacadeLocal bookingFacadeLocal;

    @InjectMocks
    private CruiseManager cruiseManager;
    private Cruise cruise1;
    private String number1 = "BARPOL123321";
    private List<Cruise> cruises;
    @Spy
    private Ferry ferry;
    @Spy
    private Route route;
    @Spy
    private Account account;
    private Cabin cabin1 = new Cabin();
    private Cabin cabin2 = new Cabin();
    private Cabin cabin3 = new Cabin();
    private Cabin cabin4 = new Cabin();

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        ferry = new Ferry();
        ferry.setName("name");
        route = new Route();
        route.setCode("123");
        account = new Account();
        account.setLogin("login");

        cruise1 = new Cruise();
        ferry.setOnDeckCapacity(120);
        cruise1.setNumber(number1);
        cruise1.setFerry(ferry);
        cruise2.setFerry(ferry);
        cruises = new ArrayList<>();
        cruises.addAll(Arrays.asList(cruise2, cruise3));
        cabin1.setCapacity(5);
        cabin2.setCapacity(7);
        cabin3.setCapacity(3);
        cabin4.setCapacity(10);
    }

    @Test
    public void getCruiseByNumber() {
        when(cruiseFacadeLocal.findByNumber(number1)).thenReturn(cruise1);
        cruise1.setCreationDate(Timestamp.from(Instant.now()));


        Cruise cruise = cruiseManager.getCruiseByNumber(number1);

        assertEquals(cruise1.getCreationDate(), cruise.getCreationDate());
        assertEquals(cruise1, cruise);
    }

    @Test
    void getAllCurrentCruises() {
        when(cruiseFacadeLocal.findAllFutureDate()).thenReturn(cruises);
        assertDoesNotThrow(() -> cruiseManager.getAllCurrentCruises());
        assertEquals(cruises.hashCode(), cruiseManager.getAllCurrentCruises().hashCode());
        verify(cruiseFacadeLocal, times(2)).findAllFutureDate();
    }

    @Test
    void getAllCurrentCruisesException() {
        doAnswer(invocationOnMock -> {
            throw CommonExceptions.createNoResultException();
        }).when(cruiseFacadeLocal).findAllFutureDate();
        assertThrows(CommonExceptions.class, () -> cruiseManager.getAllCurrentCruises());
        verify(cruiseFacadeLocal).findAllFutureDate();
    }

    @Test
    void createCruise() {
        cruise1.setCreatedBy(null);
        cruise1.setFerry(null);
        cruise1.setRoute(null);

        doAnswer(invocationOnMock -> null).when(cruiseFacadeLocal).create(cruise1);

        when(ferryFacadeLocal.findByName(ferry.getName())).thenReturn(ferry);
        when(routeFacadeLocal.findByCode(route.getCode())).thenReturn(route);
        when(accountMopFacadeLocal.findByLogin("login")).thenReturn(account);

        cruiseManager.createCruise(cruise1, ferry.getName(), route.getCode(), "login");
        Assertions.assertEquals(account, cruise1.getCreatedBy());
        Assertions.assertEquals(ferry, cruise1.getFerry());
        Assertions.assertEquals(route, cruise1.getRoute());
    }

    @Test
    void updateCruise() {
        Timestamp startDate = Timestamp.from(Instant.now());
        Timestamp endDate = Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS));

        cruise2.setVersion(10L);
        cruise2.setNumber(cruise1.getNumber());
        cruise2.setFerry(ferry);
        cruise2.setStartDate(startDate);
        cruise2.setEndDate(endDate);

        doAnswer(invocationOnMock -> {
            cruise1.setVersion(cruise2.getVersion());
            cruise1.setStartDate(cruise2.getStartDate());
            cruise1.setEndDate(cruise2.getEndDate());
            cruise1.setModifiedBy(account);
            return null;
        }).when(cruiseFacadeLocal).edit(any());

        when(cruiseFacadeLocal.findByNumber(cruise1.getNumber())).thenReturn(cruise1);
        when(accountMopFacadeLocal.findByLogin("login")).thenReturn(account);
        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(any())).thenReturn(List.of());
        when(cabinFacadeLocal.findCabinsOnCruise(any())).thenReturn(List.of());
        when(bookingFacadeLocal.getSumNumberOfPeopleByCruise(any())).thenReturn(10L);

        cruise1.setStartDate(Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS)));

        cruiseManager.updateCruise(cruise2, "login");
        Assertions.assertEquals(account, cruise1.getModifiedBy());
        Assertions.assertEquals(startDate, cruise1.getStartDate());
        Assertions.assertEquals(endDate, cruise1.getEndDate());

        cruise1.setStartDate(Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS)));

        WebApplicationException exception = assertThrows(CommonExceptions.class,
                () -> cruiseManager.updateCruise(cruise2, "login"));

        assertAll(
                () -> assertEquals(CommonExceptions.createConstraintViolationException().getResponse().getStatus(),
                        exception.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.createConstraintViolationException().getMessage(),
                        exception.getMessage())
        );
    }

    @Test
    void removeCruise() {
        when(cruiseFacadeLocal.findByNumber(number1)).thenReturn(cruise1);

        cruise1.setStartDate(Timestamp.from(Instant.now().minus(Duration.ofDays(2))));

        CruiseExceptions cruiseExceptionStarted = assertThrows(CruiseExceptions.class,
                () -> cruiseManager.removeCruise(cruise1.getNumber(), "Login"));

        assertEquals(409, cruiseExceptionStarted.getResponse().getStatus());
        assertEquals(CruiseExceptions.ERROR_CRUISE_ALREADY_STARTED, cruiseExceptionStarted.getResponse().getEntity());

        cruise1.setStartDate(Timestamp.from(Instant.now().plus(Duration.ofDays(1))));
        doAnswer(invocationOnMock -> {
            throw CruiseExceptions.createConflictException(CruiseExceptions.ERROR_CRUISE_IS_BEING_USED);
        }).when(cruiseFacadeLocal).remove(cruise1);

        CruiseExceptions cruiseExceptionUsed = assertThrows(CruiseExceptions.class,
                () -> cruiseManager.removeCruise(cruise1.getNumber(), "Login"));

        assertEquals(409, cruiseExceptionUsed.getResponse().getStatus());
        assertEquals(CruiseExceptions.ERROR_CRUISE_IS_BEING_USED, cruiseExceptionUsed.getResponse().getEntity());

        verify(cruiseFacadeLocal).remove(cruise1);
    }

    @Test
    void calculatePopularity() {
        ferry.setOnDeckCapacity(100);
        List<Cabin> takenCabins = List.of(cabin1, cabin3);
        List<Cabin> allCabins = List.of(cabin1, cabin2, cabin3, cabin4);

        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(any())).thenReturn(takenCabins);
        when(cabinFacadeLocal.findCabinsOnCruise(any())).thenReturn(allCabins);
        when(bookingFacadeLocal.getSumNumberOfPeopleByCruise(any())).thenReturn(70L);

        doAnswer(invocationOnMock -> null).when(cruiseFacadeLocal).edit(any());
        assertDoesNotThrow(() -> cruiseManager.calculatePopularity(cruise2));
        assertEquals(62.4, cruiseManager.calculatePopularity(cruise2), 0.01);

        takenCabins = List.of(cabin1, cabin3, cabin4);
        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(any())).thenReturn(takenCabins);
        when(bookingFacadeLocal.getSumNumberOfPeopleByCruise(any())).thenReturn(98L);
        assertDoesNotThrow(() -> cruiseManager.calculatePopularity(cruise2));
        assertEquals(92.8, cruiseManager.calculatePopularity(cruise2), 0.01);

        when(bookingFacadeLocal.getSumNumberOfPeopleByCruise(any())).thenReturn(7000L);
        assertDoesNotThrow(() -> cruiseManager.calculatePopularity(cruise2));
        assertEquals(100, cruiseManager.calculatePopularity(cruise2));

        takenCabins = Collections.emptyList();
        when(cabinFacadeLocal.findOccupiedCabinsOnCruise(any())).thenReturn(takenCabins);
        when(bookingFacadeLocal.getSumNumberOfPeopleByCruise(any())).thenReturn(0L);
        assertDoesNotThrow(() -> cruiseManager.calculatePopularity(cruise2));

        assertEquals(0, cruiseManager.calculatePopularity(cruise2));
    }
}
