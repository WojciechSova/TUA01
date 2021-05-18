import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ChangeEmailService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts';
    }

    confirmEmailChange(url: string): Observable<any> {
        return this.http.put(this.url + '/confirm/email/' + url, {});
    }

    changeEmail(newEmail: string): any {
        return this.http.post(this.url + '/profile/email', newEmail, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'text/plain'
            }
        });
    }

    changeOtherAccountEmail(login: string, newEmail: string): any {
        return this.http.post(this.url + '/email/' + login, newEmail, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'text/plain'
            }
        });
    }
}
