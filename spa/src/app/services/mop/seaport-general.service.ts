import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { SeaportGeneral } from '../../model/mop/SeaportGeneral';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SeaportGeneralService implements OnDestroy {

    private readonly url: string;
    seaportsList: SeaportGeneral[] = [];

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/seaports';
    }

    getSeaports(): Observable<SeaportGeneral[]> {
        return this.http.get<SeaportGeneral[]>(this.url, {
            observe: 'body',
            responseType: 'json'
        });
    }

    ngOnDestroy(): void {
        this.seaportsList = [];
    }

    addSeaport(seaport: SeaportGeneral): any {
        return this.http.post(this.url.concat('/add'), seaport, {responseType: 'text'});
    }

    deleteSeaport(code: string): any {
        return this.http.delete(this.url.concat('/remove/', code));
    }

}
