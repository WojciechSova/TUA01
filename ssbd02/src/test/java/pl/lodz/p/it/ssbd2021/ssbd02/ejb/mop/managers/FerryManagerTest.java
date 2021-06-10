package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.FerryFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FerryManagerTest {

    private final String ferryName1 = "ferry1";
    private final List<Cabin> cabins = new ArrayList<>();

    @Mock
    FerryFacadeLocal ferryFacadeLocal;

    @Mock
    CabinFacadeLocal cabinFacadeLocal;

    @InjectMocks
    FerryManager ferryManager;

    @Spy
    Ferry ferry1 = new Ferry();
    @Spy
    Ferry ferry2 = new Ferry();

    private List<Ferry> ferries;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        ferries = new ArrayList<>();
        ferries.addAll(Arrays.asList(ferry1, ferry2));
    }

    @Test
    void getAllFerriesTest() {
        when(ferryFacadeLocal.findAll()).thenReturn(ferries);

        assertEquals(ferries.hashCode(), ferryManager.getAllFerries().hashCode());
    }

    @Test
    void getAllFerriesExceptionTest() {
        when(ferryFacadeLocal.findAll()).thenReturn(null);

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
        when(ferryFacadeLocal.findByName(ferryName1)).thenReturn(null);
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
}
