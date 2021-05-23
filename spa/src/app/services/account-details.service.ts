import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AccountDetails } from '../model/mok/AccountDetails';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AccountDetailsService implements OnDestroy {

    private readonly url: string;

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
        this.account = this.parseDates(this.account);
        this.eTag = (response.headers.get('etag') as string).slice(1, -1);
    }

    private parseDates(account: AccountDetails): AccountDetails {
        account.modificationDate = this.parseDate(account.modificationDate);
        account.creationDate = (this.parseDate(account.creationDate) as Date);
        account.lastKnownBadLogin = this.parseDate(account.lastKnownBadLogin);
        account.lastKnownGoodLogin = this.parseDate(account.lastKnownGoodLogin);
        account.accessLevel.forEach(value => value.modificationDate = this.parseDate(value.modificationDate));
        account.accessLevel.forEach(value => value.creationDate = (this.parseDate(value.creationDate) as Date));
        return account;
    }

    private parseDate(stringDate: any): Date | undefined {
        if (!stringDate) {
            return undefined;
        }
        return new Date(stringDate.toString().split('[UTC]')[0]);
    }

    ngOnDestroy(): void {
        this.account = {
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

        this.eTag = '';
    }
}
