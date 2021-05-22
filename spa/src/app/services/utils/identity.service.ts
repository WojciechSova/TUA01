import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class IdentityService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts/change/accesslevel';
    }

    getLogin(): string {
        return localStorage.getItem('login') as string;
    }

    isAdmin(): boolean {
        return localStorage.getItem('currentAccessLevel') === 'ADMIN';
    }

    isEmployee(): boolean {
        return localStorage.getItem('currentAccessLevel') === 'EMPLOYEE';
    }

    isClient(): boolean {
        return localStorage.getItem('currentAccessLevel') === 'CLIENT';
    }

    isGuest(): boolean {
        return localStorage.getItem('currentAccessLevel') === null;
    }

    getAllRolesAsString(): string {
        return localStorage.getItem('accessLevel') as string;
    }

    getCurrentRole(): string {
        return localStorage.getItem('currentAccessLevel') as string;
    }

    getAllRolesAsArray(): string[] {
        const levels = this.getAllRolesAsString();
        return levels.split(',');
    }

    setCurrentRole(level: string): void {
        localStorage.setItem('currentAccessLevel', level);
        this.http.post(this.url, level, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token')
            }
        }).subscribe();
    }

    getTimezone(): string {
        return localStorage.getItem('timezone') || '+00:00';
    }
}
