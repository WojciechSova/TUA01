import { Injectable } from '@angular/core';
import { CruiseGeneral } from '../../model/mop/CruiseGeneral';

@Injectable({
  providedIn: 'root'
})
export class CruiseGeneralService {

    getCurrentCruises(): CruiseGeneral[] {
        const cruise1 = {
            startDate: new Date(),
            endDate: new Date(),
            route: {
                start: {
                    city: 'City1',
                    code: 'ABC'
                },
                destination: {
                    city: 'City2',
                    code: 'DEF'
                },
                code: 'ABCDEF'
            },
            ferry: {
                name: 'Ferry 1',
                onDeckCapacity: 100,
                vehicleCapacity: 1234
            },
            number: 'ABCDEF000001'
        };
        const cruise2 = {
            startDate: new Date(),
            endDate: new Date(),
            route: {
                start: {
                    city: 'City3',
                    code: 'CBA'
                },
                destination: {
                    city: 'City4',
                    code: 'FED'
                },
                code: 'FEDCBA'
            },
            ferry: {
                name: 'Ferry 2',
                onDeckCapacity: 100,
                vehicleCapacity: 1234
            },
            number: 'FEDCBA000001'
        };
        return [cruise1, cruise2];
    }

  constructor() { }
}
