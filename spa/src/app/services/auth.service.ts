import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import jwtDecode from 'jwt-decode';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly url: string;

    constructor(private http: HttpClient) {

        this.url = environment.appUrl + '/auth';
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
        localStorage.setItem('timezone', tokenInfo.zoneinfo);
        this.setCurrentAccessLevel(tokenInfo.auth);
    }

    signOut(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('login');
        localStorage.removeItem('currentAccessLevel');
        localStorage.removeItem('accessLevel');
    }

    private setCurrentAccessLevel(groups: string): void {
        const current = localStorage.getItem('currentAccessLevel');
        console.log(current);
        const accessLvls = groups.split(',');
        console.log(accessLvls);

        if (current && accessLvls.includes(current)) {
            return;
        }

        localStorage.setItem('currentAccessLevel', accessLvls[0]);
        return;
    }
}
