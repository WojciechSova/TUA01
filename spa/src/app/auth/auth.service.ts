import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import jwt_decode from 'jwt-decode';

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
        this.getDecodedTokenInfo(token);
    }

    getDecodedTokenInfo(token: string): any {
        const tokenInfo: any = jwt_decode(token);
        localStorage.setItem('login', tokenInfo.sub);
        localStorage.setItem('accessLevel', tokenInfo.auth);
        localStorage.setItem('currentAccessLevel', tokenInfo.auth.split(',')[0]);
    }
}
