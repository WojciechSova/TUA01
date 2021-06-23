import { Injectable, OnDestroy } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { FerryDetails } from '../../model/mop/FerryDetails';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class FerryDetailsService implements OnDestroy{

    private readonly url: string;
    popup = 'hidden';

    etag = '';

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

    private parseDates(ferryDetails: FerryDetails): FerryDetails {
        ferryDetails.modificationDate = FerryDetailsService.parseDate(ferryDetails.modificationDate);
        ferryDetails.creationDate = FerryDetailsService.parseDate(ferryDetails.creationDate);
        return ferryDetails;
    }

    getFerry(name: string): Observable<HttpResponse<FerryDetails>> {
        return this.http.get<FerryDetails>(this.url + '/' + encodeURIComponent(name), {
            observe: 'response',
            responseType: 'json'
        });
    }

    readFerryDetails(response: HttpResponse<FerryDetails>): void {
        this.ferry = response.body as FerryDetails;
        this.ferry = this.parseDates(this.ferry);
        this.etag = (response.headers.get('etag') as string).slice(1, -1);
    }

    updateFerry(changedFerry: FerryDetails): Observable<HttpResponse<any>> {
        return this.http.put<any>(this.url + '/update', changedFerry, {
            observe: 'response',
            headers: {
                'If-Match': this.etag
            }
        });
    }

    ngOnDestroy(): void {
        this.popup = 'hidden';
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
