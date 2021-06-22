import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { CruiseDetails } from '../../model/mop/CruiseDetails';
import { Observable } from 'rxjs';
import { CruiseGeneral } from '../../model/mop/CruiseGeneral';

@Injectable({
    providedIn: 'root'
})
export class CruiseDetailsService implements OnDestroy {

    cruise: CruiseDetails = this.getEmptyCruiseDetails();
    eTag = '';
    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/cruises/';
    }

    getCruise(code: string): Observable<HttpResponse<CruiseDetails>> {
        this.cruise = this.getEmptyCruiseDetails();
        return this.http.get<CruiseDetails>(this.url + code, {
            observe: 'response',
            responseType: 'json'
        });
    }

    addCruise(cruise: CruiseGeneral, ferry: string, route: string): any {
        return this.http.post(this.url + 'add/' + ferry + '/' + route, cruise, { responseType: 'text' });
    }

    readCruiseAndEtagFromResponse(response: HttpResponse<CruiseDetails>): void {
        this.cruise = response.body as CruiseDetails;
        this.cruise = this.parseDates(this.cruise);
        this.eTag = (response.headers.get('etag') as string).slice(1, -1);
    }

    private parseDates(cruise: CruiseDetails): CruiseDetails {
        cruise.startDate = (this.parseDate(cruise.startDate) as Date);
        cruise.endDate = (this.parseDate(cruise.endDate) as Date);
        cruise.modificationDate = this.parseDate(cruise.modificationDate);
        cruise.creationDate = (this.parseDate(cruise.creationDate) as Date);
        return cruise;
    }

    private parseDate(stringDate: any): Date | undefined {
        if (!stringDate) {
            return undefined;
        }
        return new Date(stringDate.toString().split('[UTC]')[ 0 ]);
    }

    ngOnDestroy(): void {
        this.cruise = this.getEmptyCruiseDetails();
    }

    private getEmptyCruiseDetails(): CruiseDetails {
        return {
            startDate: new Date(),
            endDate: new Date(),
            route: {
                start: {
                    code: '',
                    city: ''
                },
                destination: {
                    code: '',
                    city: ''
                },
                code: '',
            },
            ferry: {
                name: '',
                onDeckCapacity: 0,
                vehicleCapacity: 0,
            },
            number: '',
            popularity: 0,
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
            version: 0,
        };
    }
}
