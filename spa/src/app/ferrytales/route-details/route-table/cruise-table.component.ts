import { Component, Input, OnInit } from '@angular/core';
import { IdentityService } from '../../../services/utils/identity.service';
import { CruiseGeneral } from "../../../model/mop/CruiseGeneral";

@Component({
    selector: 'app-cruise-table',
    templateUrl: './cruise-table.component.html',
    styleUrls: ['./cruise-table.component.less']
})
export class CruiseTableComponent implements OnInit {

    constructor(public identityService: IdentityService) {
    }

    @Input()
    cruises: CruiseGeneral[] = [];

    ngOnInit(): void {
    }

}
