import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AccessLevelService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts';
    }

    addAccessLevel(login: string, accessLevel: string): any {
        return this.http.put(this.url + '/addaccesslevel/' + login, accessLevel);
    }

    removeAccessLevel(login: string, accessLevel: string): any {
        return this.http.put(this.url + '/removeaccesslevel/' + login, accessLevel);
    }
}
