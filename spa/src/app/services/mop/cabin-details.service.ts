import {Injectable, OnDestroy} from '@angular/core';
import { CabinDetails } from '../../model/mop/CabinDetails';

@Injectable({
    providedIn: 'root'
})
export class CabinDetailsService {

    cabin: CabinDetails = {
        capacity: '1',
        cabinType: "VIP",
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

    getCabin(code: string): CabinDetails {
        return this.cabin;
    }
}
