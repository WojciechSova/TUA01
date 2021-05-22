import { Injectable } from '@angular/core';
import { HttpBackend, HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthService } from '../auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    private httpClient: HttpClient;

    constructor(handler: HttpBackend,
                private authService: AuthService) {
        this.httpClient = new HttpClient(handler);
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (req.url !== environment.appUrl + '/auth' && localStorage.getItem('token') !== null) {
            this.httpClient.get(environment.appUrl + '/auth', {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }, observe: 'body', responseType: 'text'
            }).subscribe(
                (response: string) => {
                    this.authService.setSession(response);
                }
            );
        }

        return next.handle(req);
    }
}
