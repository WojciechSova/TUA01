import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AccountDetails} from '../model/mok/AccountDetails';
import {Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AccountDetailsService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = 'https://localhost:8181/ssbd02/accounts/';
    }

    getAccountDetails(login: string): Observable<AccountDetails> {
        return this.http.get<AccountDetails>(this.url + encodeURIComponent(login),
            {observe: 'body', responseType: 'json'});
    }
}
