import { Component, Input } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
    selector: 'app-links',
    templateUrl: './links.component.html',
    styleUrls: ['./links.component.less']
})
export class LinksComponent {

    @Input()
    public stickyStyles = true;

    constructor(private authService: AuthService) {
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

    getLogin(): string {
        return localStorage.getItem('login') as string;
    }

    signOut(): void {
        this.authService.signOut();
    }
}
