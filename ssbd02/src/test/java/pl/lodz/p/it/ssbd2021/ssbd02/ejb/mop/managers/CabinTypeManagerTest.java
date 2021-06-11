package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CabinTypeFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CabinTypeManagerTest {

    @Mock
    CabinTypeFacadeLocal cabinTypeFacadeLocal;

    @InjectMocks
    CabinTypeManager cabinTypeManager;

    @Spy
    CabinType cabinType = new CabinType();

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        cabinType.setCabinTypeName("Nazwa");

    }

    @Test
    void getCabinTypeByName() {
        when(cabinTypeFacadeLocal.findByName("Nazwa")).thenReturn(cabinType);
        assertEquals(cabinType, cabinTypeManager.getCabinTypeByName("Nazwa"));
        assertEquals(cabinType.hashCode(), cabinTypeManager.getCabinTypeByName("Nazwa").hashCode());
    }
}
