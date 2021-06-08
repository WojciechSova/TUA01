import { Component, OnInit } from '@angular/core';
import { RouteDetails } from '../../model/mop/RouteDetails';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-route-details',
    templateUrl: './route-details.component.html',
    styleUrls: ['./route-details.component.less']
})
export class RouteDetailsComponent implements OnInit {

    route: RouteDetails = {
        start: {
            city: "start",
            code: "111",
        },
        destination: {
            city: "dest",
            code: "222",
        },
        code: 'CODE',
        cruises: [
            {
                startDate: new Date(),
                endDate: new Date(),
                ferry: {
                    name: 'Ferry Name',
                    vehicleCapacity: 400,
                    onDeckCapacity: 1000,
                },
                number: "123",
            },
            {
                startDate: new Date(),
                endDate: new Date(),
                ferry: {
                    name: 'Ferry Name 2',
                    vehicleCapacity: 500,
                    onDeckCapacity: 700,
                },
                number: "333",
            }
        ],
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
