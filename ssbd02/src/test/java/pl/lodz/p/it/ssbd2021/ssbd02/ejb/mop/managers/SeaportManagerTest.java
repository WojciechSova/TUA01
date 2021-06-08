package pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.facades.interfaces.SeaportFacadeLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SeaportManagerTest {

    @Spy
    private final Seaport s1 = new Seaport();
    @Spy
    private final Seaport s2 = new Seaport();
    @Spy
    private final Seaport s3 = new Seaport();
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
        s3.setCity("Pabianice");
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
        when(seaportFacadeLocal.findByCode(null)).thenReturn(null);

        Seaport seaport = seaportManager.getSeaportByCode("CODE");
        assertEquals(s1, seaport);

        GeneralException exception = Assertions.assertThrows(CommonExceptions.class,
                () -> seaportManager.getSeaportByCode(null));

        Assertions.assertEquals(410, exception.getResponse().getStatus());
        Assertions.assertEquals(CommonExceptions.ERROR_NO_RESULT, exception.getResponse().getEntity());
    }

    @Test
    void createSeaport() {
        String login = "przykladowy";
        doAnswer(invocationOnMock -> {
            seaports.add(s3);
            return null;
        }).when(seaportFacadeLocal).create(s3);

        seaportManager.createSeaport(login, s3);

        assertEquals(3, seaports.size());
        assertEquals(s3.hashCode(), seaports.get(seaports.size() - 1).hashCode());
    }
}
