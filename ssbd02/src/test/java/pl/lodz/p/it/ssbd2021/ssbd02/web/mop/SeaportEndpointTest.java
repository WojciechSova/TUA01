package pl.lodz.p.it.ssbd2021.ssbd02.web.mop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.SeaportGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mop.managers.interfaces.SeaportManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Seaport;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.CommonExceptions;
import pl.lodz.p.it.ssbd2021.ssbd02.exceptions.GeneralException;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.SeaportMapper;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.signing.DTOIdentitySignerVerifier;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class SeaportEndpointTest {

    private final List<Seaport> seaports = new ArrayList<>();
    private final Seaport s1 = new Seaport();
    private final Seaport s2 = new Seaport();
    private final SeaportDetailsDTO s3 = new SeaportDetailsDTO();
    @Spy
    private Seaport seaport1;
    @Mock
    private SeaportManagerLocal seaportManager;
    @Mock
    private UserPrincipal userPrincipal;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private SeaportEndpoint seaportEndpoint;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        s1.setCity("Warszawa");
        s1.setCode("WAR");
        s2.setCity("Ciechocinek");
        s3.setCity("Pabianice");
        s3.setCode("PAB");
        seaport1 = new Seaport();
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

    @Test
    void getSeaport() {
        when(seaportManager.getSeaportByCode("123")).thenReturn(seaport1);

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

    @Test
    void addSeaport() {
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn("Przykladowy");
        doAnswer(invocationOnMock -> {
            seaports.add(SeaportMapper.createSeaportFromSeaportDetailsDTO(s3));
            return null;
        }).when(seaportManager).createSeaport("Przykladowy", SeaportMapper.createSeaportFromSeaportDetailsDTO(s3));

        Response response = seaportEndpoint.addSeaport(s3, securityContext);

        s3.setCity(null);
        WebApplicationException exception = assertThrows(CommonExceptions.class, () -> seaportEndpoint.addSeaport(s3, securityContext));

        assertAll(
                () -> assertEquals(Response.Status.OK.getStatusCode(), response.getStatus()),
                () -> assertEquals(400, exception.getResponse().getStatus()),
                () -> assertEquals(CommonExceptions.ERROR_CONSTRAINT_VIOLATION, exception.getResponse().getEntity())
        );
    }

    @Test
    void updateSeaport() {
        Account account = new Account();
        account.setLogin("Login");
        when(securityContext.getUserPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getName()).thenReturn(account.getLogin());

        s1.setVersion(1L);
        SeaportDetailsDTO seaportDetailsDTO = new SeaportDetailsDTO();
        seaportDetailsDTO.setVersion(1L);
        seaportDetailsDTO.setCity("newName");
        seaportDetailsDTO.setCode("WAR");
        String eTag = DTOIdentitySignerVerifier.calculateDTOSignature(seaportDetailsDTO);

        doAnswer(invocationOnMock -> {
            s1.setCity("newName");
            s1.setVersion(2L);
            s1.setModifiedBy(account);
            return null;
        }).when(seaportManager).updateSeaport(SeaportMapper.createSeaportFromSeaportDetailsDTO(seaportDetailsDTO), "Login");

        Response response = seaportEndpoint.updateSeaport(seaportDetailsDTO, securityContext, eTag);

        GeneralException badEtag = assertThrows(GeneralException.class,
                () -> seaportEndpoint.updateSeaport(seaportDetailsDTO, securityContext, "not.an.etag"));

        seaportDetailsDTO.setCity(null);
        GeneralException noCity = assertThrows(GeneralException.class,
                () -> seaportEndpoint.updateSeaport(seaportDetailsDTO, securityContext, eTag));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), badEtag.getResponse().getStatus());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), noCity.getResponse().getStatus());
    }
}
