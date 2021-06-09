package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CruiseManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CruiseMapper;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CruiseEndpointTest {

    @Spy
    private final Cruise cruise1 = new Cruise();
    private final List<Cruise> currentCruises = Collections.singletonList(cruise1);
    @Mock
    private CruiseManagerLocal cruiseManagerLocal;
    @InjectMocks
    private CruiseEndpoint cruiseEndpoint;

    private Cruise cruise;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        cruise = new Cruise();
        cruise.setNumber("111");
    }

    @Test
    void getCruise() {
        when(cruiseManagerLocal.getCruiseByNumber("111")).thenReturn(cruise);

        Response response = cruiseEndpoint.getCruise("111");

        assertEquals(CruiseMapper.createCruiseDetailsDTOFromEntity(cruise), response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void getCurrentCruises() {
        when(cruiseManagerLocal.getAllCurrentCruises()).thenReturn(currentCruises);

        List<CruiseGeneralDTO> currentCruisesDTOList = currentCruises.stream()
                .map(CruiseMapper::createCruiseGeneralDTOFromEntity)
                .collect(Collectors.toList());

        assertAll(
                () -> assertDoesNotThrow(() -> cruiseEndpoint.getCurrentCruises()),
                () -> assertEquals(currentCruisesDTOList.hashCode(), cruiseEndpoint.getCurrentCruises().getEntity().hashCode()),
                () -> assertEquals(Response.Status.OK.getStatusCode(), cruiseEndpoint.getCurrentCruises().getStatus())
        );

        verify(cruiseManagerLocal, times(3)).getAllCurrentCruises();
    }
}
