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
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.CabinType;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;
import pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers.FerryMapper;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FerryEndpointTest {

    @Spy
    private final Ferry ferry1 = new Ferry();
    @Spy
    private final Ferry ferry2 = new Ferry();
    @Mock
    private final Ferry ferry = new Ferry();
    private final String ferryName = "FerryName";
    @Mock
    private final Cabin cabin = new Cabin();
    @Mock
    private final CabinType cabinType = new CabinType();
    @Mock
    private FerryManagerLocal ferryManagerLocal;
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
}
