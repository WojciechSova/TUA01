import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class AccountGeneralService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = 'https://studapp.it.p.lodz.pl:8402/ssbd02/accounts';
    }

    getAccounts(): any {
        return this.http.get<any>(this.url, {observe: 'body', responseType: 'json'});
    }
}
