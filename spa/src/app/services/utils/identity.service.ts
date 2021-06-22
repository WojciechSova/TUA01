import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { CookieService } from 'ngx-cookie-service';

@Injectable()
export class IdentityService {

    private readonly url: string;

    constructor(private http: HttpClient,
                private cookieService: CookieService) {
        this.url = environment.appUrl + '/accounts/change/accesslevel';
    }

    getLogin(): string {
        return this.cookieService.get('login') as string;
    }

    isAdmin(): boolean {
        return this.cookieService.get('currentAccessLevel') === 'ADMIN';
    }

    isEmployee(): boolean {
        return this.cookieService.get('currentAccessLevel') === 'EMPLOYEE';
    }

    isClient(): boolean {
        return this.cookieService.get('currentAccessLevel') === 'CLIENT';
    }

    isGuest(): boolean {
        return this.cookieService.get('currentAccessLevel') === '';
    }

    getAllRolesAsString(): string {
        return this.cookieService.get('accessLevel') as string;
    }

    getCurrentRole(): string {
        return this.cookieService.get('currentAccessLevel') as string;
    }

    setCurrentRole(level: string): void {
        this.cookieService.set('currentAccessLevel', level);
        this.http.post(this.url, level).subscribe();
    }

    getTimezone(): string {
        const date = new Date();
        const indexOfGMT = date.toTimeString().indexOf('GMT');
        return this.cookieService.get('timezone') || date.toTimeString().substring(indexOfGMT + 3, indexOfGMT + 6) + ':00';
    }
}
