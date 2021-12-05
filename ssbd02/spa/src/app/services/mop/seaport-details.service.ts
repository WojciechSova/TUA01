import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {SeaportDetails} from '../../model/mop/SeaportDetails';
import {Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SeaportDetailsService implements OnDestroy {

    private readonly url: string;

    etag = '';

    seaport: SeaportDetails = {
        city: '',
        code: '',
        modificationDate: new Date(),
        creationDate: new Date(),
        createdBy: {
            login: '',
            active: true,
            firstName: '',
            lastName: '',
            accessLevel: ['']
        }
    };

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/seaports';
    }

    getSeaportByCode(code: string): Observable<HttpResponse<SeaportDetails>> {
        return this.http.get<SeaportDetails>(this.url + '/' + code, {
            observe: 'response',
            responseType: 'json'
        });
    }

    readSeaportDetails(response: HttpResponse<SeaportDetails>): void {
        this.seaport = response.body as SeaportDetails;
        this.seaport = this.parseDates(this.seaport);
        this.etag = (response.headers.get('etag') as string).slice(1, -1);
    }

    private parseDates(seaportDetails: SeaportDetails): SeaportDetails {
        seaportDetails.modificationDate = this.parseDate(seaportDetails.modificationDate);
        seaportDetails.creationDate = this.parseDate(seaportDetails.creationDate);
        return seaportDetails;
    }

    private parseDate(stringDate: any): any {
        if (!stringDate) {
            return undefined;
        }
        return new Date(stringDate.toString().split('[UTC]')[ 0 ]);
    }

    updateSeaport(changedSeaport: SeaportDetails): Observable<HttpResponse<any>> {
        return this.http.put<any>(this.url + '/update', changedSeaport, {
            observe: 'response',
            headers: {
                'If-Match': this.etag
            }
        });
    }

    ngOnDestroy(): void {
        this.etag = '';
        this.seaport = {
            city: '',
            code: '',
            modificationDate: new Date(),
            creationDate: new Date(),
            createdBy: {
                login: '',
                active: true,
                firstName: '',
                lastName: '',
                accessLevel: ['']
            }
        };
    }
}
