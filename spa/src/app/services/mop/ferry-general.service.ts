import { Injectable, OnDestroy } from '@angular/core';
import { FerryGeneral } from '../../model/mop/FerryGeneral';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class FerryGeneralService implements OnDestroy {

    ferriesGeneralList: FerryGeneral[] = [];
    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/ferries';
    }

    getFerries(): any {
        return this.http.get<any>(this.url, {
            observe: 'body',
            responseType: 'json'
        });
    }

    addFerry(ferry: FerryGeneral): any {
        return this.http.post(this.url.concat('/add'), ferry);
    }

    ngOnDestroy(): void {
        this.ferriesGeneralList = [];
    }


}
