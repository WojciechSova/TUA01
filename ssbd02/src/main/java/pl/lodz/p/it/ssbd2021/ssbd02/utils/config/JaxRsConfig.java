package pl.lodz.p.it.ssbd2021.ssbd02.utils.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Klasa rozszerzająca {@link Application}.
 * Adnotacja {@link ApplicationPath} określa ścieżkę aplikacji, która służy jako bazowe URI dla punktów dostępowych.
 *
 * @author Kacper Świercz
 */
@ApplicationPath("ssbd02")
public class JaxRsConfig extends Application {
}
