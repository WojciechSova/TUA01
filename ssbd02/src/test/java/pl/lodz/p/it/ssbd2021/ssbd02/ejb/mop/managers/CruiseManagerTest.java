package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.Assertions;
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
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.RouteFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
        cruise1.setNumber(number1);
        cruises = new ArrayList<>();
        cruises.addAll(Arrays.asList(cruise2, cruise3));
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
}
