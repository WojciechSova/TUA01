package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.SeaportManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.SeaportMapper;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SeaportEndpointTest {

    private final List<Seaport> seaports = new ArrayList<>();
    @Mock
    private SeaportManagerLocal seaportManager;
    @InjectMocks
    private SeaportEndpoint seaportEndpoint;
    private final Seaport s1 = new Seaport();
    private final Seaport s2 = new Seaport();

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        s1.setCity("Warszawa");
        s2.setCity("Ciechocinek");
    }

    @Test
    void getAllSeaports() {
        seaports.add(s1);
        seaports.add(s2);
        List<SeaportGeneralDTO> expectedDTOList = seaports.stream()
                .map(SeaportMapper::createSeaportGeneralDTOFromEntities)
                .collect(Collectors.toList());
        when(seaportManager.getAllSeaports()).thenReturn(seaports);

        Response response = seaportEndpoint.getAllSeaports();

        assertEquals(expectedDTOList, response.getEntity());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}






