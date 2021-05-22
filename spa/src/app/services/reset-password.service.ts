import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class ResetPasswordService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts/reset/password';
    }

    resetPassword(email: string): any {
        return this.http.post(this.url, email);
    }

    setNewPassword(oneTimeUrl: string, newPassword: string): Observable<HttpResponse<any>> {
        const completeUrl = this.url + '/' + oneTimeUrl;
        return this.http.put<any>(completeUrl, newPassword);
    }

    resetPasswordResponse(email: string): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.url, email,
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('token')
                }
            });
    }
}
