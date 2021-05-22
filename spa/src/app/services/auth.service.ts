import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import jwtDecode from 'jwt-decode';
import { environment } from '../../environments/environment';
import { SessionUtilsService } from './utils/session-utils.service';
import { AccountDetailsService } from './account-details.service';
import { AccountGeneralService } from './account-general.service';
import { UpdateAccountService } from './update-account.service';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly url: string;

    constructor(private http: HttpClient,
                private sessionUtilsService: SessionUtilsService,
                private accountDetailsService: AccountDetailsService,
                private accountGeneralService: AccountGeneralService,
                private updateAccountService: UpdateAccountService,
                private router: Router) {
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
        localStorage.setItem('currentAccessLevel', tokenInfo.auth.split(',')[0]);
        localStorage.setItem('expirationTime', tokenInfo.exp);
        this.sessionUtilsService.setSessionTimeout(tokenInfo.exp);
    }

    signOut(): void {
        this.router.navigate(['/']);
        this.accountDetailsService.ngOnDestroy();
        this.accountGeneralService.ngOnDestroy();
        this.updateAccountService.ngOnDestroy();
        localStorage.removeItem('token');
        localStorage.removeItem('login');
        localStorage.removeItem('currentAccessLevel');
        localStorage.removeItem('accessLevel');
        localStorage.removeItem('expirationTime');
        this.sessionUtilsService.clearSessionTimeout();
    }
}
