import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { SeaportDetails } from '../../model/mop/SeaportDetails';

@Injectable({
    providedIn: 'root'
})
export class SeaportDetailsService implements OnDestroy {

    private readonly url: string;

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

    getSeaportByCode(code: string): any {
        return this.http.get<any>(this.url + '/' + code, {
            observe: 'body',
            responseType: 'json'
        });
    }

    readSeaportDetails(response: SeaportDetails): void {
        this.seaport = response;
        this.seaport = this.parseDates(this.seaport);
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

    ngOnDestroy(): void {
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
