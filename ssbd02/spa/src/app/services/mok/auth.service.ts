import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import jwtDecode from 'jwt-decode';
import { environment } from '../../../environments/environment';
import { SessionUtilsService } from '../utils/session-utils.service';
import { AccountDetailsService } from './account-details.service';
import { AccountGeneralService } from './account-general.service';
import { UpdateAccountService } from './update-account.service';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { SeaportGeneralService } from '../mop/seaport-general.service';
import { SeaportDetailsService } from '../mop/seaport-details.service';
import { RouteDetailsService } from '../mop/route-details.service';
import { CruiseDetailsService } from '../mop/cruise-details.service';
import { CruiseGeneralService } from '../mop/cruise-general.service';
import { FerryDetailsService } from '../mop/ferry-details.service';
import { FerryGeneralService } from '../mop/ferry-general.service';
import { BookingGeneralService } from '../mop/booking-general.service';
import { BookingDetailsService } from '../mop/booking-details.service';
import { CabinDetailsService } from '../mop/cabin-details.service';

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
                private seaportGeneralService: SeaportGeneralService,
                private seaportDetailsService: SeaportDetailsService,
                private routeDetailsService: RouteDetailsService,
                private cruiseDetailsService: CruiseDetailsService,
                private ferryGeneralService: FerryGeneralService,
                private ferryDetailsService: FerryDetailsService,
                private cruiseGeneralService: CruiseGeneralService,
                private bookingGeneralService: BookingGeneralService,
                private bookingDetailsService: BookingDetailsService,
                private cabinDetailsService: CabinDetailsService,
                private cookieService: CookieService,
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
        this.cookieService.set('token', token);
        this.decodeTokenInfo(token);
    }

    decodeTokenInfo(token: string): void {
        const tokenInfo: any = jwtDecode(token);
        this.cookieService.set('login', tokenInfo.sub);
        this.cookieService.set('accessLevel', tokenInfo.auth);
        this.cookieService.set('timezone', tokenInfo.zoneinfo);
        this.setCurrentAccessLevel(tokenInfo.auth);
        this.cookieService.set('expirationTime', tokenInfo.exp);
        this.sessionUtilsService.setSessionTimeout(tokenInfo.exp);
        this.sessionUtilsService.setSessionNearlyTimeout(tokenInfo.exp);
    }

    signOut(): void {
        this.router.navigate(['/']);
        this.accountDetailsService.ngOnDestroy();
        this.accountGeneralService.ngOnDestroy();
        this.updateAccountService.ngOnDestroy();
        this.seaportGeneralService.ngOnDestroy();
        this.seaportDetailsService.ngOnDestroy();
        this.routeDetailsService.ngOnDestroy();
        this.cruiseDetailsService.ngOnDestroy();
        this.ferryGeneralService.ngOnDestroy();
        this.ferryDetailsService.ngOnDestroy();
        this.bookingGeneralService.ngOnDestroy();
        this.bookingDetailsService.ngOnDestroy();
        this.cabinDetailsService.ngOnDestroy();
        this.cookieService.delete('token');
        this.cookieService.delete('login');
        this.cookieService.delete('currentAccessLevel');
        this.cookieService.delete('accessLevel');
        this.cookieService.delete('expirationTime');
        this.cookieService.delete('timezone');
        this.sessionUtilsService.clearSessionTimeout();
        this.sessionUtilsService.clearSessionNearlyTimeout();
    }

    private setCurrentAccessLevel(groups: string): void {
        const current = this.cookieService.get('currentAccessLevel');
        const accessLvls = groups.split(',');

        if (current && accessLvls.includes(current)) {
            return;
        }

        this.cookieService.set('currentAccessLevel', accessLvls[0]);
        return;
    }

    public refreshToken(): void {
        this.http.get(environment.appUrl + '/auth', {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token')
            }, observe: 'body', responseType: 'text'
        }).subscribe(
            (response: string) => {
                this.setSession(response);
            }
        );
        this.sessionUtilsService.clearSessionNearlyTimeout();
    }
}
