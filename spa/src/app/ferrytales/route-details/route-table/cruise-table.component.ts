import { Component, Input, OnInit } from '@angular/core';
import { IdentityService } from '../../../services/utils/identity.service';
import { CruiseGeneral } from '../../../model/mop/CruiseGeneral';
import { Router } from '@angular/router';

@Component({
    selector: 'app-cruise-table',
    templateUrl: './cruise-table.component.html',
    styleUrls: ['./cruise-table.component.less']
})
export class CruiseTableComponent implements OnInit {

    constructor(public identityService: IdentityService,
                private router: Router) {
    }

    @Input()
    cruises: CruiseGeneral[] = [];

    ngOnInit(): void {
    }

    cruiseDetails(cruiseNumber: string): void {
        this.router.navigate([`/ferrytales/cruises/${cruiseNumber}`]);
    }
}
