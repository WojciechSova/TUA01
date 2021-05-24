import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AccountDetails } from '../model/mok/AccountDetails';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class RegistrationService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts/register';
    }

    register(account: AccountDetails): any {
        return this.http.post(this.url, account, {responseType: 'text'});
    }
}
