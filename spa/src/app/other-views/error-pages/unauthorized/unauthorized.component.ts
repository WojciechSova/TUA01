import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../../../services/mok/auth.service';

@Component({
    selector: 'app-unauthorized',
    templateUrl: './unauthorized.component.html',
    styleUrls: ['./unauthorized.component.less']
})
export class UnauthorizedComponent implements OnInit {

    constructor(private router: Router,
                private cookieService: CookieService,
                private authService: AuthService) {
        const expirationTime = cookieService.get('expirationTime');
        if (expirationTime != null && expirationTime !== '' && expirationTime < (Date.now().valueOf() / 1000).toString()) {
            this.authService.signOut();
            this.router.navigate(['/']);
        }
    }

    goToHomePage(): void {
        this.router.navigate(['/']);
    }

    ngOnInit(): void {
    }
}
