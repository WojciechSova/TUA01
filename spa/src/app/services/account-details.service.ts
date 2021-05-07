import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AccountDetails } from '../model/mok/AccountDetails';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AccountDetailsService implements OnDestroy {

    public account: AccountDetails = {
        accessLevel: [],
        active: false,
        confirmed: false,
        creationDate: new Date(),
        email: '',
        firstName: '',
        lastName: '',
        numberOfBadLogins: 0,
        login: '',
        password: ''
    };

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts';
    }

    getAccountDetails(login: string): Observable<AccountDetails> {
        return this.http.get<AccountDetails>(this.url + encodeURIComponent(login),
            {
                observe: 'body',
                responseType: 'json',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }

    getProfile(): Observable<AccountDetails> {
        return this.http.get<AccountDetails>(this.url + 'profile',
            {
                observe: 'body',
                responseType: 'json',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }

    ngOnDestroy(): void {
        this.account = {} as any;
    }
}
