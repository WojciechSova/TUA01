package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.VehicleTypeDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.VehicleType;

/**
 * Klasa mapująca obiekty typów pojazdów pomiędzy encjami, a DTO
 *
 * @author Patryk Kolanek
 */
public class VehicleTypeMapper {

    /**
     * Metoda mapująca obiekt {@link VehicleType} na obiekt {@link VehicleTypeDTO}
     *
     * @param vehicleType Obiekt {@link VehicleType}, który chcemy mapować
     * @return Obiekt typu {@link VehicleTypeDTO}
     */
    public static VehicleTypeDTO createVehicleTypeDTOFromEntity(VehicleType vehicleType) {
        if (vehicleType == null) {
            return null;
        }

        VehicleTypeDTO vehicleTypeDTO = new VehicleTypeDTO();
        vehicleTypeDTO.setVehicleTypeName(vehicleType.getVehicleTypeName());
        vehicleTypeDTO.setRequiredSpace(vehicleType.getRequiredSpace());
        return vehicleTypeDTO;
    }
}
