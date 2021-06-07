package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;

/**
 * Klasa mapująca obiekty trasy pomiędzy encjami a DTO.
 *
 * @author Daniel Łondka
 */
public class RouteMapper {

    /**
     * Metoda mapująca obiekt encji {@link Route} na obiekt DTO {@link RouteGeneralDTO}
     *
     * @param route Obiekt typu {@link Route} na podstawie którego tworzony jest DTO
     * @return Obiekt typu {@link RouteGeneralDTO}
     */
    public static RouteGeneralDTO createRouteGeneralDTOFromEntity(Route route) {
        if (route == null) {
            return null;
        }
        RouteGeneralDTO routeGeneralDTO = new RouteGeneralDTO();

        routeGeneralDTO.setCode(route.getCode());
        routeGeneralDTO.setStart(SeaportMapper.createSeaportGeneralDTOFromEntities(route.getStart()));
        routeGeneralDTO.setDestination(SeaportMapper.createSeaportGeneralDTOFromEntities(route.getDestination()));
        routeGeneralDTO.setVersion(route.getVersion());

        return routeGeneralDTO;
    }
}
