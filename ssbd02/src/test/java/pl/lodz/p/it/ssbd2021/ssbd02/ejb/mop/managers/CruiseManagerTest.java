package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;

import java.sql.Timestamp;
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

    @InjectMocks
    private CruiseManager cruiseManager;
    private Cruise cruise1;
    private String number1 = "BARPOL123321";
    private List<Cruise> cruises;


    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

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
        when(cruiseFacadeLocal.findAllFutureDate()).thenReturn(null);
        assertThrows(CommonExceptions.class, () -> cruiseManager.getAllCurrentCruises());
        verify(cruiseFacadeLocal).findAllFutureDate();
    }
}
