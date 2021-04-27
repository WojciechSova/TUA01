package pl.lodz.p.it.ssbd2021.ssbd02.utils.config;

import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.AccountManager;
import pl.lodz.p.it.ssbd2021.ssbd02.ejb.mok.managers.interfaces.AccountManagerLocal;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/account")
public class AccountService {

    @Inject
    AccountManagerLocal accountManagerLocal;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public void add(Account account) {
        accountManagerLocal.createAccount(account);
    }
}
