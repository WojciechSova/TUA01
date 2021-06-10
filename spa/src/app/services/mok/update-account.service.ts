import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AccountDetails } from '../../model/mok/AccountDetails';
import { Observable } from 'rxjs';
import { AccountDetailsService } from './account-details.service';

@Injectable({
    providedIn: 'root'
})
export class UpdateAccountService implements OnDestroy {

    private readonly url: string;

    constructor(private http: HttpClient,
                private accountDetailsService: AccountDetailsService) {
        this.url = environment.appUrl + '/accounts';
    }

    getAccountETag(login: string): Observable<HttpResponse<AccountDetails>> {
        return this.http.get<AccountDetails>(this.url + '/' + encodeURIComponent(login),
            {
                observe: 'response',
                responseType: 'json'
            });
    }

    updateAccount(account: AccountDetails): Observable<object> {
        return this.sendUpdateAccountRequest(account, '/update');
    }

    updateOwnAccount(account: AccountDetails): Observable<object> {
        return this.sendUpdateAccountRequest(account, '/profile/update');
    }

    sendUpdateAccountRequest(account: AccountDetails, urlPart: string): Observable<object> {
        const completeUrl = this.url + urlPart;
        if (account.phoneNumber === '') {
            account.phoneNumber = undefined;
        }
        return this.http.put(completeUrl, account,
            {
                observe: 'body',
                responseType: 'json',
                headers: {
                    'If-Match': this.accountDetailsService.eTag
                }
            });
    }

    ngOnDestroy(): void {}
}
