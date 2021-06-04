import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SeaportDetails } from '../../model/mop/SeaportDetails';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-seaport',
    templateUrl: './seaport-details.component.html',
    styleUrls: ['./seaport-details.component.less']
})
export class SeaportDetailsComponent implements OnInit {

    code = '';

    seaport: SeaportDetails = {
        city: '',
        code: '',
        modificationDate: new Date(),
        creationDate: new Date(),
    };

    constructor(private route: ActivatedRoute,
                public identityService: IdentityService) {
        this.code = this.route.snapshot.paramMap.get('code') as string;
        this.getSeaport();
    }

    ngOnInit(): void {
    }

    getSeaport(): void {
        // TODO Move Seaport to Service (?)
        this.seaport = {
            city: 'City',
            code: 'Code',
            modificationDate: new Date(),
            modifiedBy: {
                login: 'Login',
                active: true,
                firstName: 'First Name',
                lastName: 'Last Name',
                accessLevel: ['ADMIN']
            },
            creationDate: new Date(),
            createdBy: {
                login: 'Login',
                active: true,
                firstName: 'First Name',
                lastName: 'Last Name',
                accessLevel: ['ADMIN']
            }
        };
    }
}
