import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CabinGeneral } from '../../model/mop/CabinGeneral';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CabinGeneralService {

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/cabins';
    }

    addCabin(cabin: CabinGeneral, ferryName: string): any {
        return this.http.post(this.url.concat('/' + ferryName).concat('/add'), cabin, {responseType: 'text'});
    }

    getFreeCabinsOnCruise(cruiseNumber: string): any{
        return this.http.get(this.url + '/cruise/free/' + cruiseNumber, {
            observe: 'body',
            responseType: 'json'
        });
    }
}
