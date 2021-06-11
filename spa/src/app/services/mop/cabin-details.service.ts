import {Injectable, OnDestroy} from '@angular/core';
import {CabinDetails} from '../../model/mop/CabinDetails';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class CabinDetailsService implements OnDestroy {

    cabin: CabinDetails = {
        capacity: '1',
        cabinType: 'First class',
        number: 'ABCXYZ100200',
        modificationDate: new Date(),
        modifiedBy: undefined,
        creationDate: new Date(),
        createdBy: {
            login: 'string',
            active: true,
            firstName: 'Adam',
            lastName: 'Adam',
            accessLevel: [],
        },
    };

    eTag = '';
    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/cabins';
    }

    getCabin(code: string): CabinDetails {
        return this.cabin;
    }

    updateCabin(cabin: CabinDetails): Observable<object> {
        const completeUrl = this.url + '/update';
        return this.http.put(completeUrl, cabin,
            {
                observe: 'body',
                responseType: 'json',
                headers: {
                    'If-Match': this.eTag
                }
            });
    }

    ngOnDestroy(): void {
        this.cabin = {
            capacity: '',
            cabinType: '',
            number: '',
            modificationDate: new Date(),
            modifiedBy: undefined,
            creationDate: new Date(),
            createdBy: {
                login: '',
                active: false,
                firstName: '',
                lastName: '',
                accessLevel: [],
            }
        };
    }
}
