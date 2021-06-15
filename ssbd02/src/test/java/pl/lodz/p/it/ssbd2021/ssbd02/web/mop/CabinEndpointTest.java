package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CabinDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CabinTypeManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CabinMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class CabinEndpointTest {

    private final String ferryName = "Perl";
    @Mock
    private CabinManagerLocal cabinManagerLocal;
    @Mock
    private CabinTypeManagerLocal cabinTypeManagerLocal;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserPrincipal userPrincipal;
    @InjectMocks
    private CabinEndpoint cabinEndpoint;
    @Spy
    private Cabin cabin;
    @Spy
    private Cabin cabin2;
    @Spy
    private CabinType cabinType;
    @Spy
    private CabinType cabinType2;
    private List<Cabin> cabins;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        cabin = new Cabin();
        cabin.setNumber("123");
        cabin.setVersion(1L);
        cabinType = new CabinType();
        cabinType.setCabinTypeName("Luksus");
        cabinType2 = new CabinType();
        cabinType2.setCabinTypeName("Normal");
        cabinType2.setCabinTypeName("Normal");

        cabin2.setVersion(1L);
        cabin2.setCapacity(15);
        cabin2.setNumber("A555");
        cabin2.setCabinType(cabinType2);
        cabins = new ArrayList<>();
        cabins.add(cabin);
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

    @Test
    void addCabin() {
        doAnswer(invocationOnMock -> {
            cabins.add(cabin2);
            return null;
        }).when(cabinManagerLocal).createCabin(any(), any(), any());
        when(cabinTypeManagerLocal.getCabinTypeByName(cabinType2.getCabinTypeName())).thenReturn(cabinType2);
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        Response response = cabinEndpoint.addCabin(CabinMapper.createCabinDetailsDTOFromEntity(cabin2), securityContext, ferryName);

        assertAll(
                () -> assertEquals(2, cabins.size()),
                () -> assertEquals(cabin2, cabins.get(cabins.size() - 1)),
                () -> assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus())
        );
    }

    @Test
    void updateCabin() {
        doAnswer(invocationOnMock -> {
            cabin.setVersion(1L);
            cabin.setCabinType(cabinType);
            cabin.setCapacity(10);
            return null;
        }).when(cabinManagerLocal).updateCabin(any(), any());
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(cabinTypeManagerLocal.getCabinTypeByName(any())).thenReturn(cabinType);

        cabin.setVersion(0L);
        cabin.setCapacity(12);
        cabin.setCabinType(cabinType2);

        String tag = DTOIdentitySignerVerifier.calculateDTOSignature(CabinMapper
                .createCabinDetailsDTOFromEntity(cabin));

        assertEquals(0L, cabin.getVersion());

        Response response = cabinEndpoint.updateCabin(CabinMapper.createCabinDetailsDTOFromEntity(cabin), securityContext, tag);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(1L, cabin.getVersion());
        assertEquals(10, cabin.getCapacity());
        assertEquals(cabinType.getCabinTypeName(), cabin.getCabinType().getCabinTypeName());
    }
}
