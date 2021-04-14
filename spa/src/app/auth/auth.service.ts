import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import jwtDecode from 'jwt-decode';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient) {
    }

    auth(login: string, password: string): any {
        return this.http.post('https://studapp.it.p.lodz.pl:8402/ssbd02/auth', {
            login,
            password
        }, {observe: 'body', responseType: 'text'});
    }

    public setSession(token: string): void {
        localStorage.setItem('token', token);
        this.decodeTokenInfo(token);
    }

    decodeTokenInfo(token: string): void {
        const tokenInfo: any = jwtDecode(token);
        localStorage.setItem('login', tokenInfo.sub);
        localStorage.setItem('accessLevel', tokenInfo.auth);
        localStorage.setItem('currentAccessLevel', tokenInfo.auth.split(',')[0]);
    }
}
