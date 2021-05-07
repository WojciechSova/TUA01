import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ChangePasswordService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts/password';
    }

    changePassword(oldPassword: string, newPassword: string): any {
        return this.http.put(this.url, {
            oldPassword,
            newPassword
        }, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token')
            }
        });
    }
}
