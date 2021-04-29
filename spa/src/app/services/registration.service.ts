import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { AccountDetails } from "../model/mok/AccountDetails";

@Injectable({
    providedIn: 'root'
})
export class RegistrationService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = 'https://studapp.it.p.lodz.pl:8402/ssbd02/accounts/register';
    }

    register(account: AccountDetails): void {
        this.http.post<any>(this.url, {
           account
        }, { observe: 'body'});
    }
}
