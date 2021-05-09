import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AccountDetails } from '../model/mok/AccountDetails';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class UpdateAccountService implements OnDestroy {

    private readonly url: string;
    public eTag = '';

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts';
    }

    getAccountETag(login: string): Observable<HttpResponse<AccountDetails>> {
        return this.http.get<AccountDetails>(this.url + '/' + encodeURIComponent(login),
            {
                observe: 'response',
                responseType: 'json',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }

    updateAccount(account: AccountDetails): Observable<Object> {
        return this.http.put(this.url + '/update', account,
            {
                observe: 'body',
                responseType: 'json',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token'),
                    'If-Match': this.eTag.substring(1, this.eTag.length)
                }
            });
    }

    ngOnDestroy(): void {
        this.eTag = {} as any;
    }
}
