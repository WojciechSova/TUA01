import { Injectable, OnDestroy } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { FerryDetails } from '../../model/mop/FerryDetails';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class FerryDetailsService implements OnDestroy{

    private readonly url: string;

    ferry: FerryDetails = {
        name: '',
        cabins: [],
        vehicleCapacity: '',
        onDeckCapacity: '',
        creationDate: new Date(),
        createdBy: {
            login: '',
            active: true,
            firstName: '',
            lastName: '',
            accessLevel: []
        }
    };

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/ferries';
    }

    private static parseDate(stringDate: any): any {
        if (!stringDate) {
            return undefined;
        }
        return new Date(stringDate.toString().split('[UTC]')[ 0 ]);
    }

    private static parseDates(ferryDetails: FerryDetails): FerryDetails {
        ferryDetails.modificationDate = FerryDetailsService.parseDate(ferryDetails.modificationDate);
        ferryDetails.creationDate = FerryDetailsService.parseDate(ferryDetails.creationDate);
        return ferryDetails;
    }

    getFerry(name: string): Observable<FerryDetails> {
        return this.http.get<FerryDetails>(this.url + '/' + encodeURIComponent(name), {
            observe: 'body',
            responseType: 'json'
        });
    }

    readFerryDetails(response: FerryDetails): void {
        this.ferry = response;
        this.ferry = FerryDetailsService.parseDates(this.ferry);
    }

    ngOnDestroy(): void {
        this.ferry = {
            name: '',
            cabins: [],
            vehicleCapacity: '',
            onDeckCapacity: '',
            creationDate: new Date(),
            createdBy: {
                login: '',
                active: true,
                firstName: '',
                lastName: '',
                accessLevel: []
            }
        };
    }
}
