import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AccountDetails } from '../model/mok/AccountDetails';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

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

    eTag = '';

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts';
    }

    getAccountDetails(login: string): Observable<HttpResponse<AccountDetails>> {
        return this.generateAccountDetailsRequest(login);
    }

    getProfile(): Observable<HttpResponse<AccountDetails>> {
        return this.generateAccountDetailsRequest();

    }

    private generateAccountDetailsRequest(login?: string): Observable<HttpResponse<AccountDetails>> {
        const urlBuilder = this.url + ((login === undefined || login === null) ?
            '/profile' : ('/' + encodeURIComponent(login as string)));
        return this.http.get<AccountDetails>(urlBuilder,
            {
                observe: 'response',
                responseType: 'json',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }

    readAccountAndEtagFromResponse(response: HttpResponse<AccountDetails>): void {
        this.account = response.body as AccountDetails;
        this.eTag = (response.headers.get('etag') as string).slice(1, -1);
    }

    ngOnDestroy(): void {
        this.account = {} as any;
        this.eTag = '';
    }
}
