package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.FerryManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.FerryMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FerryEndpointTest {

    @Spy
    private final Ferry ferry1 = new Ferry();
    @Spy
    private final Ferry ferry2 = new Ferry();
    @Mock
    private final Ferry ferry = new Ferry();
    private final FerryDetailsDTO ferry3 = new FerryDetailsDTO();
    private final String ferryName = "FerryName";
    @Mock
    private final Cabin cabin = new Cabin();
    @Mock
    private final CabinType cabinType = new CabinType();
    @Mock
    private FerryManagerLocal ferryManagerLocal;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserPrincipal userPrincipal;
    @InjectMocks
    private FerryEndpoint ferryEndpoint;
    private List<Ferry> ferries;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);

        ferries = new ArrayList<>();
        ferries.addAll(Arrays.asList(ferry1, ferry2));
        when(ferry1.getName()).thenReturn("Prom1");
        when(ferry1.getOnDeckCapacity()).thenReturn(50);
        when(ferry1.getVehicleCapacity()).thenReturn(20);
        when(ferry1.getVersion()).thenReturn(1L);
        when(ferry2.getName()).thenReturn("Prom2");
        when(ferry2.getOnDeckCapacity()).thenReturn(500);
        when(ferry2.getVehicleCapacity()).thenReturn(200);
        when(ferry2.getVersion()).thenReturn(2L);

        ferry3.setName("Nowy prom");
        ferry3.setVehicleCapacity(100);
        ferry3.setOnDeckCapacity(1000);
    }

    @Test
    public void getAllFerriesTest() {
        when(ferryManagerLocal.getAllFerries()).thenReturn(ferries);
        List<FerryGeneralDTO> ferryGeneralDTOS = ferries.stream()
                .map(FerryMapper::createFerryGeneralDTOFromEntity)
                .collect(Collectors.toList());

        assertAll(
                () -> assertEquals(ferryGeneralDTOS.hashCode(), ferryEndpoint.getAllFerries().getEntity().hashCode()),
                () -> assertEquals(Response.Status.OK.getStatusCode(), ferryEndpoint.getAllFerries().getStatus())
        );
    }

    @Test
    void getFerry() {
        when(ferryManagerLocal.getFerryAndCabinsByFerryName(ferryName)).thenReturn(Pair.of(ferry, Collections.singletonList(cabin)));
        when(cabin.getCabinType()).thenReturn(cabinType);

        FerryDetailsDTO ferryDetailsDTO = FerryMapper.createFerryDetailsDTOFromEntities(Pair.of(ferry, Collections.singletonList(cabin)));

        assertAll(
                () -> assertEquals(ferryDetailsDTO.hashCode(), ferryEndpoint.getFerry(ferryName).getEntity().hashCode()),
                () -> assertEquals(Response.Status.OK.getStatusCode(), ferryEndpoint.getFerry(ferryName).getStatus())
        );
        verify(ferryManagerLocal, times(2)).getFerryAndCabinsByFerryName(ferryName);
    }

    @Test
    void addFerry() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("Login");
        doAnswer(invocationOnMock -> {
            ferries.add(FerryMapper.createFerryFromFerryDetailsDTO(ferry3));
            return null;
        }).when(ferryManagerLocal).createFerry("Login", FerryMapper.createFerryFromFerryDetailsDTO(ferry3));

        Response response = assertDoesNotThrow(() -> ferryEndpoint.addFerry(ferry3, securityContext));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(ferryManagerLocal).createFerry(anyString(), any());

        ferry3.setName(null);
        GeneralException ex1 = assertThrows(CommonExceptions.class, () -> ferryEndpoint.addFerry(ferry3, securityContext));
        ferry3.setVehicleCapacity(null);
        GeneralException ex2 = assertThrows(CommonExceptions.class, () -> ferryEndpoint.addFerry(ferry3, securityContext));
        ferry3.setOnDeckCapacity(null);
        GeneralException ex3 = assertThrows(CommonExceptions.class, () -> ferryEndpoint.addFerry(ferry3, securityContext));
        assertAll(
                () -> assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, ex1.getResponse().getEntity()),
                () -> assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex1.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, ex2.getResponse().getEntity()),
                () -> assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex2.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, ex3.getResponse().getEntity()),
                () -> assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), ex3.getResponse().getStatus())
        );
    }

    @Test
    void updateFerry() {
        Account account = new Account();
        account.setLogin("Login");
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("Login");

        Ferry f1 = new Ferry();
        f1.setVersion(1L);
        FerryDetailsDTO ferryDetailsDTO = new FerryDetailsDTO();
        ferryDetailsDTO.setVersion(1L);
        ferryDetailsDTO.setName("newName");
        ferryDetailsDTO.setOnDeckCapacity(10);
        ferryDetailsDTO.setVehicleCapacity(20);
        String eTag = DTOIdentitySignerVerifier.calculateDTOSignature(ferryDetailsDTO);

        doAnswer(invocationOnMock -> null).when(ferryManagerLocal).updateFerry(any(), anyString());

        Response response = ferryEndpoint.updateFerry(ferryDetailsDTO, securityContext, eTag);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(ferryManagerLocal).updateFerry(any(), anyString());

        GeneralException badEtag = assertThrows(GeneralException.class,
                () -> ferryEndpoint.updateFerry(ferryDetailsDTO, securityContext, "not.an.etag"));
        assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), badEtag.getResponse().getStatus());

        ferryDetailsDTO.setName(null);
        GeneralException noName = assertThrows(GeneralException.class,
                () -> ferryEndpoint.updateFerry(ferryDetailsDTO, securityContext, eTag));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), noName.getResponse().getStatus());
    }
}
