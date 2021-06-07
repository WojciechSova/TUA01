import { Component, Input, OnInit } from '@angular/core';
import { IdentityService } from '../../../services/utils/identity.service';
import { CabinGeneral } from '../../../model/mop/CabinGeneral';

@Component({
    selector: 'app-cabin-table',
    templateUrl: './cabin-table.component.html',
    styleUrls: ['./cabin-table.component.less']
})
export class CabinTableComponent implements OnInit {

    constructor(public identityService: IdentityService) {
    }

    @Input()
    cabins: CabinGeneral[] = [];

    ngOnInit(): void {
    }

}
