package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.CruiseFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CruiseManagerTest {

    @Mock
    private CruiseFacadeLocal cruiseFacadeLocal;

    @InjectMocks
    private CruiseManager cruiseManager;

    private Cruise cruise1;
    private String number1 = "BARPOL123321";

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        cruise1 = new Cruise();
        cruise1.setNumber(number1);
    }

    @Test
    public void getCruiseByNumber() {
        when(cruiseFacadeLocal.findByNumber(number1)).thenReturn(cruise1);
        cruise1.setCreationDate(Timestamp.from(Instant.now()));


        Cruise cruise = cruiseManager.getCruiseByNumber(number1);

        assertEquals(cruise1.getCreationDate(), cruise.getCreationDate());
        assertEquals(cruise1, cruise);
    }
}
