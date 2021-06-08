import {Injectable, OnDestroy} from '@angular/core';
import {CabinDetails} from '../../model/mop/CabinDetails';

@Injectable({
    providedIn: 'root'
})
export class CabinDetailsService implements OnDestroy {


    public cabin: CabinDetails = {
        capacity: '',
        cabinType: '',
        number: '',
        creationDate: new Date()
    };

    eTag = '';

    constructor() {
    }

    ngOnDestroy(): void {
        this.cabin = {
            capacity: '',
            cabinType: '',
            number: '',
            creationDate: new Date()
        };

        this.eTag = '';
    }
}
