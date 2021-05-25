import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from '../auth.service';
import { CookieService } from 'ngx-cookie-service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    private httpClient: HttpClient;

    constructor(handler: HttpBackend,
                private authService: AuthService,
                private cookieService: CookieService) {
        this.httpClient = new HttpClient(handler);
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (req.url !== environment.appUrl + '/auth' && this.cookieService.get('token') !== null) {
            this.httpClient.get(environment.appUrl + '/auth', {
                headers: {
                    Authorization: 'Bearer ' + this.cookieService.get('token')
                }, observe: 'body', responseType: 'text'
            }).subscribe(
                (response: string) => {
                    this.authService.setSession(response);
                }
            );
        }

        const authReq = req.clone({setHeaders: {Authorization: 'Bearer ' + this.cookieService.get('token')}});
        return next.handle(authReq);
    }
}
