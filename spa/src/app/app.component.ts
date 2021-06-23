import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.less']
})
export class AppComponent {
    constructor(public translateService: TranslateService,
                private router: Router,
                private cookieService: CookieService) {
        const expirationTime = cookieService.get('expirationTime');
        if (expirationTime != null && expirationTime !== '' && expirationTime < (Date.now().valueOf() / 1000).toString()) {
            this.cookieService.delete('token');
            this.cookieService.delete('login');
            this.cookieService.delete('currentAccessLevel');
            this.cookieService.delete('accessLevel');
            this.cookieService.delete('expirationTime');
            this.cookieService.delete('timezone');
            this.cookieService.delete('JSESSIONID');
            this.cookieService.deleteAll();
            this.router.navigate(['/']);
        }
        translateService.addLangs(['en', 'pl']);
        translateService.setDefaultLang('en');
        const browserLang = translateService.getBrowserLang();
        translateService.use(browserLang.match(/en|pl/) ? browserLang : 'en');
    }
}
