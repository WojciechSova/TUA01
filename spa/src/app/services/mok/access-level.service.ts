import {Injectable, OnDestroy} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AccessLevelService implements OnDestroy{

    private readonly url: string;
    popup = 'hidden';

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/accounts';
    }

    addAccessLevel(login: string, accessLevel: string): any {
        return this.http.put(this.url + '/addaccesslevel/' + login, accessLevel);
    }

    removeAccessLevel(login: string, accessLevel: string): any {
        return this.http.put(this.url + '/removeaccesslevel/' + login, accessLevel);
    }

    ngOnDestroy(): void {
        this.popup = 'hidden';
    }
}
