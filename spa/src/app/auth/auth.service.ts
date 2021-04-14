import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient) {
    }

    auth(login: string, password: string): any {
        return this.http.post('https://localhost:8181/ssbd02/auth', {
            login,
            password
        }, {observe: 'body', responseType: 'text'});
    }

    public setSession(token: string): void {
        localStorage.setItem('token', token);
    }
}
