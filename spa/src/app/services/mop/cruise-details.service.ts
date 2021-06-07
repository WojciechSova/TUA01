import { Injectable } from '@angular/core';
import { FerryGeneral } from '../../model/mop/FerryGeneral';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import {CruiseDetails} from '../../model/mop/CruiseDetails';
import {AccountGeneral} from '../../model/mok/AccountGeneral';

@Injectable({
  providedIn: 'root'
})
export class CruiseDetailsService {

    cruise: CruiseDetails = {
        startDate: new Date(),
        endDate: new Date(),
        route: {
            start: 'start',
            destination: 'dest',
            code: 'COD',
        },
        ferry: {
            name: 'name',
            onDeckCapacity: 2,
            vehicleCapacity: 10,
        },
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
    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/cruises/cruise';
    }

    getCruise(code: string): void {
        return;
    }
}
