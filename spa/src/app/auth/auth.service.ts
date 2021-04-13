import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient) {
    }

    auth(login: string | undefined, password: string | undefined): any {
        return this.http.post('https://localhost:8181/ssbd02/auth', {
            "login": login,
            "password": password
        }, {observe: 'body', responseType: 'text'});
    }

    public setSession(token: string) {
        localStorage.setItem('token', token);
    }
}
