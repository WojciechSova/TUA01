import { Component, OnInit } from '@angular/core';
import { CruiseGeneral } from '../../model/mop/CruiseGeneral';
import { CruiseGeneralService } from '../../services/mop/cruise-general.service';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-current-cruises-table',
    templateUrl: './current-cruises-table.component.html',
    styleUrls: ['./current-cruises-table.component.less']
})
export class CurrentCruisesTableComponent implements OnInit {

    currentCruises: CruiseGeneral[] = [];

    constructor(private cruiseGeneralService: CruiseGeneralService,
                public identityService: IdentityService) {
        this.getCurrentCruises();
    }

    ngOnInit(): void {
    }

    getCurrentCruises(): void {
        this.currentCruises = this.cruiseGeneralService.getCurrentCruises();
    }
}
