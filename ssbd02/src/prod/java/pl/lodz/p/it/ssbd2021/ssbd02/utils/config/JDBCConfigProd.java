package pl.lodz.p.it.ssbd2021.ssbd02.utils.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd02admin",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd02admin",
        password = "admin_password",
        serverName = "studdev.it.p.lodz.pl",
        portNumber = 5432,
        databaseName = "ssbd02",
        initialPoolSize = 1,
        minPoolSize = 0,
        maxPoolSize = 1,
        maxIdleTime = 10
)
@DataSourceDefinition(
        name = "java:app/jdbc/ssbd02auth",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd02auth",
        password = "auth_password",
        serverName = "studdev.it.p.lodz.pl",
        portNumber = 5432,
        databaseName = "ssbd02"
)
@DataSourceDefinition(
        name = "java:app/jdbc/ssbd02mok",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd02mok",
        password = "mok_password",
        serverName = "studdev.it.p.lodz.pl",
        portNumber = 5432,
        databaseName = "ssbd02",
        transactional = true,
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED
)
@DataSourceDefinition(
        name = "java:app/jdbc/ssbd02mop",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "ssbd02mop",
        password = "mop_password",
        serverName = "studdev.it.p.lodz.pl",
        portNumber = 5432,
        databaseName = "ssbd02",
        transactional = true,
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED
)
@Stateless
public class JDBCConfigProd {
    @PersistenceContext(unitName = "ssbd02adminPU")
    EntityManager entityManager;
}
