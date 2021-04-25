import { Injectable } from '@angular/core';

@Injectable()
export class IdentityService {

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
}
