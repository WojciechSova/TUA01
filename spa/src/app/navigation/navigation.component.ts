import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.less']
})
export class NavigationComponent implements AfterViewInit {

    @ViewChild('logo')
    private logo: ElementRef;

    @ViewChild('navigation')
    private navigation: ElementRef;

    constructor(private authService: AuthService) {
        this.logo = new ElementRef('logo');
        this.navigation = new ElementRef('navigation');
    }

    ngAfterViewInit(): void {
        const obsOptions: any = {
            root: null,
            threshold: 0
        };

        const observer = new IntersectionObserver(this.showNavbarOnScroll.bind(this), obsOptions);
        observer.observe(this.logo?.nativeElement);
    }

    showNavbarOnScroll(entries: any): void {
        if (!entries[0].isIntersecting) {
            this.navigation?.nativeElement.classList.add('nav-sticky');
            this.navigation?.nativeElement.classList.remove('nav-non-sticky');
        } else {
            this.navigation?.nativeElement.classList.add('nav-non-sticky');
            this.navigation?.nativeElement.classList.remove('nav-sticky');
        }
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
