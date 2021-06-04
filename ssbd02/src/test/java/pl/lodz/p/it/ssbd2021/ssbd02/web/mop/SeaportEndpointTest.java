package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

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
