import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {SeaportGeneral} from '../../model/mop/SeaportGeneral';

@Injectable({
    providedIn: 'root'
})
export class SeaportGeneralService implements OnDestroy{

    private readonly url: string;
    seaportsList: SeaportGeneral[] = [];

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/seaports';
    }

    getSeaports(): any {
        return this.http.get<any>(this.url, {
            observe: 'body',
            responseType: 'json'
        });
    }

    ngOnDestroy(): void {
        this.seaportsList = [];
    }

}
