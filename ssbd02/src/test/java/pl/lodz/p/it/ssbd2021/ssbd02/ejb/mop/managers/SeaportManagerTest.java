package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.SeaportFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SeaportManagerTest {

    @Spy
    private final Seaport s1 = new Seaport();
    @Spy
    private final Seaport s2 = new Seaport();
    @Mock
    private SeaportFacadeLocal seaportFacadeLocal;
    @InjectMocks
    private SeaportManager seaportManager;
    private List<Seaport> seaports;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        s1.setCity("Warszawa");
        s2.setCity("Ciechocinek");
        seaports = new ArrayList<>();
        seaports.add(s1);
        seaports.add(s2);

        when(seaportFacadeLocal.findAll()).thenReturn(seaports);
    }

    @Test
    void getAllSeaports() {
        List<Seaport> testedSeaports = seaportManager.getAllSeaports();

        assertEquals(2, testedSeaports.size());
        assertEquals("Warszawa", testedSeaports.get(0).getCity());
        assertEquals("Ciechocinek", testedSeaports.get(1).getCity());

        verify(seaportFacadeLocal).findAll();
    }

    @Test
    void getSeaportByCode() {
        when(seaportFacadeLocal.findByCode("CODE")).thenReturn(s1);

        Seaport seaport = seaportFacadeLocal.findByCode("CODE");
        assertEquals(s1, seaport);
    }
}
