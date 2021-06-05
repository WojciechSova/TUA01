import { Component, Input, OnInit } from '@angular/core';
import { Cabin } from '../../../model/mop/Cabin';
import { IdentityService } from '../../../services/utils/identity.service';

@Component({
    selector: 'app-cabin-table',
    templateUrl: './cabin-table.component.html',
    styleUrls: ['./cabin-table.component.less']
})
export class CabinTableComponent implements OnInit {

    constructor(public identityService: IdentityService) {
    }

    @Input()
    cabins: Cabin[] = [];

    ngOnInit(): void {
    }

}
