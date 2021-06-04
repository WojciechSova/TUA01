package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.Assertions;
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
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.SeaportManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mok.AccountExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.SeaportMapper;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SeaportEndpointTest {

    @Spy
    private Seaport seaport1;

    @Mock
    private SeaportManagerLocal seaportManagerLocal;

    @InjectMocks
    private SeaportEndpoint seaportEndpoint;
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







        seaport1 = new Seaport();
    }

    @Test
    void getSeaport() {
        when(seaportManagerLocal.getSeaportByCode("123")).thenReturn(seaport1);

        SeaportDetailsDTO seaportDetailsDTO = SeaportMapper.createSeaportDetailsDTOFromEntity(seaport1);

        assertAll(
                () -> assertEquals(seaportDetailsDTO.hashCode(), seaportEndpoint.getSeaport("123").getEntity().hashCode()),
                () -> assertEquals(Response.Status.OK.getStatusCode(), seaportEndpoint.getSeaport("123").getStatus())
        );

        GeneralException exception = Assertions.assertThrows(CommonExceptions.class,
                () -> seaportEndpoint.getSeaport(null));

        Assertions.assertEquals(400, exception.getResponse().getStatus());
        Assertions.assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception.getResponse().getEntity());

        exception = Assertions.assertThrows(CommonExceptions.class,
                () -> seaportEndpoint.getSeaport("1234"));

        Assertions.assertEquals(400, exception.getResponse().getStatus());
        Assertions.assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception.getResponse().getEntity());
    }
}
