import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ConfirmService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts';
    }

    confirmAccount(url: string): Observable<any> {
        return this.http.put(this.url + '/confirm/account/' + url, {});
    }
}
