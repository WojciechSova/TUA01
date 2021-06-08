package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CabinManagerTest {

    @Mock
    CabinFacadeLocal cabinFacadeLocal;

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
}
