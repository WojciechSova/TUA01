package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CruiseManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CruiseMapper;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CruiseEndpointTest {

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
}
