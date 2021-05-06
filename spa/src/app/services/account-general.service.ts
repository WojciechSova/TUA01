import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AccountGeneral } from '../model/mok/AccountGeneral';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AccountGeneralService implements OnDestroy {

    private readonly url: string;
    accountGeneralList: AccountGeneral[] = [];


    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts';
    }

    getAccounts(): any {
        return this.http.get<any>(this.url, {observe: 'body', responseType: 'json'});
    }

    ngOnDestroy(): void {
        this.accountGeneralList = {} as any;
    }
}
