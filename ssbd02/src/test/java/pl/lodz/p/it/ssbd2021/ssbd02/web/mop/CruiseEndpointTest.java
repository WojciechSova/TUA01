package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.CruiseManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CruiseMapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CruiseEndpointTest {

    @Spy
    private final Cruise cruise1 = new Cruise();
    private List<Cruise> currentCruises = Collections.singletonList(cruise1);
    @Mock
    private CruiseManagerLocal cruiseManagerLocal;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserPrincipal userPrincipal;
    @InjectMocks
    private CruiseEndpoint cruiseEndpoint;

    private Cruise cruise;
    private CruiseDetailsDTO cruiseDetailsDTO;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        cruise = new Cruise();
        cruise.setNumber("111");
        cruiseDetailsDTO = new CruiseDetailsDTO();
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

    @Test
    void addCruise() {
        currentCruises = new ArrayList<>();

        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("login");

        doAnswer(invocationOnMock -> {
            currentCruises.add(cruise);
            return null;
        }).when(cruiseManagerLocal)
                .createCruise(CruiseMapper.createCruiseFromCruiseDetailsDTO(cruiseDetailsDTO), "ferry", "ROUTEE", "login");

        Response response = cruiseEndpoint.addCruise(cruiseDetailsDTO, "ferry", "ROUTEE", securityContext);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(currentCruises.contains(cruise));

        WebApplicationException exception = assertThrows(CommonExceptions.class,
                () -> cruiseEndpoint.addCruise(cruiseDetailsDTO, "ferry", "INVALID", securityContext));

        assertAll(
                () -> assertEquals(400, exception.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception.getResponse().getEntity())
        );

        WebApplicationException exception1 = assertThrows(CommonExceptions.class,
                () -> cruiseEndpoint.addCruise(cruiseDetailsDTO, "INVALIDINVALIDINVALIDINVALIDINVALID", "VALIDD", securityContext));

        assertAll(
                () -> assertEquals(400, exception1.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception1.getResponse().getEntity())
        );
    }

    @Test
    void removeCruise() {
        cruise.setStartDate(Timestamp.from(Instant.now().minus(Duration.ofDays(2))));
        cruise.setNumber("BLABLA000001");
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("Login");

        doAnswer(invocationOnMock -> {
            throw CruiseExceptions.createConflictException(CruiseExceptions.ERROR_CRUISE_ALREADY_STARTED);
        }).when(cruiseManagerLocal).removeCruise(cruise.getNumber(), "Login");

        CommonExceptions constraintViolationException = assertThrows(CommonExceptions.class,
                () -> cruiseEndpoint.removeCruise("notValidNumber", securityContext));

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                constraintViolationException.getResponse().getStatus());
        assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION,
                constraintViolationException.getResponse().getEntity());

        CruiseExceptions alreadyStartedException = assertThrows(CruiseExceptions.class,
                () -> cruiseEndpoint.removeCruise(cruise.getNumber(), securityContext));

        assertEquals(Response.Status.CONFLICT.getStatusCode(), alreadyStartedException.getResponse().getStatus());
        assertEquals(CruiseExceptions.ERROR_CRUISE_ALREADY_STARTED, alreadyStartedException.getResponse().getEntity());

        verify(cruiseManagerLocal).removeCruise(cruise.getNumber(), "Login");
    }
}
