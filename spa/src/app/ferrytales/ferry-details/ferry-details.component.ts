import { Component, OnInit } from '@angular/core';
import { FerryDetails } from '../../model/mop/FerryDetails';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-ferry-details',
    templateUrl: './ferry-details.component.html',
    styleUrls: ['./ferry-details.component.less']
})
export class FerryDetailsComponent implements OnInit {

    ferry: FerryDetails = {
        name: 'Ferry Name',
        cabins: [
            {
                capacity: '100',
                cabinType: 'First class',
                number: 'J123',
            },
            {
                capacity: '1000',
                cabinType: 'Second class',
                number: 'J124'
            },
        ],
        vehicleCapacity: '400',
        onDeckCapacity: '1000',
        creationDate: new Date(),
        createdBy: {
            login: 'Employee',
            active: true,
            firstName: 'FirstName',
            lastName: 'LastName',
            accessLevel: ['EMPLOYEE']
        }
    };

    constructor(public identityService: IdentityService) {
    }

    ngOnInit(): void {
    }

}
