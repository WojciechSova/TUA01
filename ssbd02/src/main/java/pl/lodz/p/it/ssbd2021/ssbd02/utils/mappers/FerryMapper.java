package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import org.apache.commons.lang3.tuple.Pair;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.FerryGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cabin;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Ferry;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa mapująca obiekty promów pomiędzy encjami a DTO
 *
 * @author Artur Madaj
 */
public class FerryMapper {

    /**
     * Metoda mapująca obiekt typu {@link Ferry} na obiekt typu {@link FerryGeneralDTO}.
     *
     * @param ferry Obiekt typu {@link Ferry}, który będzie mapowany.
     * @return Obiekt typu {@link FerryGeneralDTO}
     */
    public static FerryGeneralDTO createFerryGeneralDTOFromEntity(Ferry ferry) {
        if (ferry == null) {
            return null;
        }

        FerryGeneralDTO ferryGeneralDTO = new FerryGeneralDTO();
        ferryGeneralDTO.setName(ferry.getName());
        ferryGeneralDTO.setOnDeckCapacity(ferry.getOnDeckCapacity());
        ferryGeneralDTO.setVehicleCapacity(ferry.getVehicleCapacity());
        ferryGeneralDTO.setVersion(ferry.getVersion());
        return ferryGeneralDTO;
    }

    /**
     * Metoda mapująca parę obiektów: obiekt typu {@link Ferry} oraz lista obiektów typu {@link Cabin}
     * na obiekt typu {@link FerryDetailsDTO}
     *
     * @param pair Para obiektów: obiekt typu {@link Ferry} oraz lista obiektów typu {@link Cabin}, która będzie mapowana
     * @return Obiekt typu {@link FerryDetailsDTO}
     */
    public static FerryDetailsDTO createFerryDetailsDTOFromEntities(Pair<Ferry, List<Cabin>> pair) {
        Ferry ferry = pair.getLeft();
        List<Cabin> cabins = pair.getRight();

        FerryDetailsDTO ferryDetailsDTO = new FerryDetailsDTO();
        ferryDetailsDTO.setVersion(ferry.getVersion());
        ferryDetailsDTO.setName(ferry.getName());
        ferryDetailsDTO.setCabins(cabins.stream()
                .map(CabinMapper::createCabinDTOFromEntity)
                .collect(Collectors.toList()));
        ferryDetailsDTO.setVehicleCapacity(ferry.getVehicleCapacity());
        ferryDetailsDTO.setOnDeckCapacity(ferry.getOnDeckCapacity());
        ferryDetailsDTO.setModificationDate(ferry.getModificationDate());
        ferryDetailsDTO.setModifiedBy(AccountMapper.createAccountGeneralDTOFromEntity(ferry.getModifiedBy()));
        ferryDetailsDTO.setCreationDate(ferry.getCreationDate());
        ferryDetailsDTO.setCreatedBy(AccountMapper.createAccountGeneralDTOFromEntity(ferry.getCreatedBy()));
        return ferryDetailsDTO;
    }
}
