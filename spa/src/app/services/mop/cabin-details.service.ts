import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { CabinDetails } from '../../model/mop/CabinDetails';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class CabinDetailsService implements OnDestroy{

    cabin: CabinDetails  = this.getEmptyCabinDetails();
    eTag = '';
    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/cabins/';
    }

    getCabin(ferryName: string, cabinNumber: string): Observable<HttpResponse<CabinDetails>> {
        this.cabin = this.getEmptyCabinDetails();
        return this.http.get<CabinDetails>(this.url + 'details/' + ferryName + '/' + cabinNumber, {
            observe: 'response',
            responseType: 'json'
        });
    }

    readCabinAndEtagFromResponse(response: HttpResponse<CabinDetails>): void {
        this.cabin = response.body as CabinDetails;
        this.cabin = this.parseDates(this.cabin);
        this.eTag = (response.headers.get('etag') as string).slice(1, -1);
    }

    private parseDates(cabin: CabinDetails): CabinDetails {
        cabin.modificationDate = this.parseDate(cabin.modificationDate);
        cabin.creationDate = (this.parseDate(cabin.creationDate) as Date);
        return cabin;
    }

    private parseDate(stringDate: any): Date | undefined {
        if (!stringDate) {
            return undefined;
        }
        return new Date(stringDate.toString().split('[UTC]')[ 0 ]);
    }

    ngOnDestroy(): void {
        this.cabin = this.getEmptyCabinDetails();
    }

    private getEmptyCabinDetails(): CabinDetails {
        return {
            capacity: '',
            cabinType: '',
            number: '',
            modificationDate: new Date(),
            modifiedBy: undefined,
            creationDate: new Date(),
            createdBy: {
                login: '',
                active: true,
                firstName: '',
                lastName: '',
                accessLevel: [],
            },
        };
    }

    updateCabin(cabin: CabinDetails, ferryName: string): Observable<object> {
        const completeUrl = this.url + 'update/' + ferryName;
        return this.http.put(completeUrl, cabin,
            {
                observe: 'body',
                responseType: 'json',
                headers: {
                    'If-Match': this.eTag
                }
            });
    }

}
