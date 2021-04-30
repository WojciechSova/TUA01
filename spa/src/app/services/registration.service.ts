import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { AccountDetails } from "../model/mok/AccountDetails";

@Injectable({
    providedIn: 'root'
})
export class RegistrationService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = 'https://localhost:8181/ssbd02/accounts/register';
    }

    register(account: AccountDetails): void {
        this.http.post(this.url, {
           account
        }).subscribe(() => alert("No wysłało"));
        console.log("Registracja" + account);
    }
}
