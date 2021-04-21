import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import jwtDecode from 'jwt-decode';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = 'https://localhost:8181/ssbd02/auth';
    }

    auth(login: string, password: string): any {
        return this.http.post(this.url, {
            login,
            password
        }, { observe: 'body', responseType: 'text' });
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

    signOut(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('login');
        localStorage.removeItem('currentAccessLevel');
        localStorage.removeItem('accessLevel');
    }
}
