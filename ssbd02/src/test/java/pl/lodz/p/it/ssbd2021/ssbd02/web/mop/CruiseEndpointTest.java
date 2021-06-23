package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

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
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.mop.CruiseExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.CruiseMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        cruise1.setPopularity(50D);
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
        cruise1.setPopularity(12D);
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
        Timestamp startDate = Timestamp.from(Instant.now());
        Timestamp endDate = Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS));

        cruiseDetailsDTO.setStartDate(startDate);
        cruiseDetailsDTO.setEndDate(endDate);

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

        cruiseDetailsDTO.setStartDate(endDate);
        cruiseDetailsDTO.setEndDate(startDate);

        WebApplicationException exception2 = assertThrows(CommonExceptions.class,
                () -> cruiseEndpoint.addCruise(cruiseDetailsDTO, "ferry", "VALIDD", securityContext));

        assertAll(
                () -> assertEquals(400, exception2.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception2.getResponse().getEntity())
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

    @Test
    void updateCruise() {
        Timestamp startDate = Timestamp.from(Instant.now());
        Timestamp endDate = Timestamp.from(Instant.now().plus(1, ChronoUnit.HOURS));

        Timestamp oldStartDate = Timestamp.from(Instant.now().plus(10, ChronoUnit.DAYS));
        Timestamp oldEndDate = Timestamp.from(Instant.now().plus(12, ChronoUnit.DAYS));

        cruiseDetailsDTO.setStartDate(startDate);
        cruiseDetailsDTO.setEndDate(endDate);
        cruiseDetailsDTO.setVersion(0L);

        cruise.setStartDate(oldStartDate);
        cruise.setEndDate(oldEndDate);

        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("login");

        doAnswer(invocationOnMock -> {
            cruise.setStartDate(startDate);
            cruise.setEndDate(endDate);
            return null;
        }).when(cruiseManagerLocal)
                .updateCruise(CruiseMapper.createCruiseFromCruiseDetailsDTO(cruiseDetailsDTO),"login");

        String eTag = DTOIdentitySignerVerifier.calculateDTOSignature(cruiseDetailsDTO);

        Response response = cruiseEndpoint.updateCruise(cruiseDetailsDTO, securityContext, eTag);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(cruiseDetailsDTO.getStartDate(), cruise.getStartDate());
        assertEquals(cruiseDetailsDTO.getEndDate(), cruise.getEndDate());

        GeneralException exception = assertThrows(GeneralException.class,
                () -> cruiseEndpoint.updateCruise(cruiseDetailsDTO, securityContext, "not.an.etag"));

        assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), exception.getResponse().getStatus());

        cruiseDetailsDTO.setStartDate(endDate);
        cruiseDetailsDTO.setEndDate(startDate);

        WebApplicationException exception2 = assertThrows(CommonExceptions.class,
                () -> cruiseEndpoint.updateCruise(cruiseDetailsDTO, securityContext, eTag));

        assertAll(
                () -> assertEquals(400, exception2.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception2.getResponse().getEntity())
        );
    }
}
