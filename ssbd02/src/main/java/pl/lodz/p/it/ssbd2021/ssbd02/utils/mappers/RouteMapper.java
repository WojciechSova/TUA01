package pl.lodz.p.it.ssbd2021.ssbd02.utils.mappers;

import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.CruiseGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteDetailsDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.dto.mop.RouteGeneralDTO;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Cruise;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mop.Route;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Metoda mapująca obiekt encji {@link Route} oraz listę encji {@link Cruise} na obiekt DTO {@link RouteDetailsDTO}
     *
     * @param route   Obiekt typu {@link Route} na podstawie którego tworzony jest DTO
     * @param cruises Lista obiektów typu {@link Cruise} na podstawie której tworzony jest DTO
     * @return Obiekt typu {@link RouteDetailsDTO}
     */
    public static RouteDetailsDTO createRouteDetailsDTOFromEntity(Route route, List<Cruise> cruises) {
        if (route == null) {
            return null;
        }
        RouteDetailsDTO routeDetailsDTO = new RouteDetailsDTO();

        routeDetailsDTO.setCode(route.getCode());
        routeDetailsDTO.setStart(SeaportMapper.createSeaportGeneralDTOFromEntities(route.getStart()));
        routeDetailsDTO.setDestination(SeaportMapper.createSeaportGeneralDTOFromEntities(route.getDestination()));
        routeDetailsDTO.setCreationDate(route.getCreationDate());
        routeDetailsDTO.setCreatedBy(AccountMapper.createAccountGeneralDTOFromEntity(route.getCreatedBy()));
        routeDetailsDTO.setVersion(route.getVersion());

        List<CruiseGeneralDTO> cruiseGeneralDTOList = cruises.stream()
                .map(CruiseMapper::createCruiseGeneralDTOFromEntity)
                .collect(Collectors.toList());
        routeDetailsDTO.setCruises(cruiseGeneralDTOList);

        return routeDetailsDTO;
    }

    /**
     * Metoda mapująca obiekt encji {@link RouteGeneralDTO} na obiekt typu {@link Route}
     *
     * @param routeGeneralDTO Obiekt typu {@link RouteGeneralDTO}, który będzie mapowany.
     * @return Obiekt typu {@link Route}
     */
    public static Route createRouteFromRouteGeneralDTO(RouteGeneralDTO routeGeneralDTO) {
        if (routeGeneralDTO == null) {
            return null;
        }

        Route route = new Route();

        route.setCode(routeGeneralDTO.getCode());
        route.setStart(SeaportMapper.createSeaportFromSeaportGeneralDTO(routeGeneralDTO.getStart()));
        route.setDestination(SeaportMapper.createSeaportFromSeaportGeneralDTO(routeGeneralDTO.getDestination()));
        route.setVersion(route.getVersion());

        return route;
    }
}
