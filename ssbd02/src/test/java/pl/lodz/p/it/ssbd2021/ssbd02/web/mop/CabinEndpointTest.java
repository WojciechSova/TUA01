package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CabinMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class CabinEndpointTest {

    @Mock
    private CabinManagerLocal cabinManagerLocal;
    @InjectMocks
    private CabinEndpoint cabinEndpoint;

    private Cabin cabin;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        cabin = new Cabin();
        cabin.setNumber("123");
        cabin.setVersion(1L);
    }

    @Test
    void getCabin() {
        when(cabinManagerLocal.getCabinByNumber("123")).thenReturn(cabin);

        Response response = cabinEndpoint.getCabin("123");

        assertEquals(CabinMapper.createCabinDetailsDTOFromEntity(cabin), response.getEntity());
        assertTrue(DTOIdentitySignerVerifier.verifyDTOIntegrity(response.getEntityTag().getValue(),
                ((CabinDetailsDTO) response.getEntity())));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
