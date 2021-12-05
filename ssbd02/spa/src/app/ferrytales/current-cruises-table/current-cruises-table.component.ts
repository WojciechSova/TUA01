import { Component, OnInit } from '@angular/core';
import { CruiseGeneral } from '../../model/mop/CruiseGeneral';
import { CruiseGeneralService } from '../../services/mop/cruise-general.service';
import { IdentityService } from '../../services/utils/identity.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-current-cruises-table',
    templateUrl: './current-cruises-table.component.html',
    styleUrls: ['./current-cruises-table.component.less']
})
export class CurrentCruisesTableComponent implements OnInit {

    constructor(public identityService: IdentityService,
                private cruiseGeneralService: CruiseGeneralService,
                private router: Router) {
        this.getCurrentCruises();
    }

    ngOnInit(): void {
    }

    getCurrentCruises(): void {
        this.cruiseGeneralService.getCurrentCruises().subscribe(
            (response: CruiseGeneral[]) => {
                this.cruiseGeneralService.readCurrentCruises(response);
            });
    }

    listCurrentCruises(): CruiseGeneral[] {
        return this.cruiseGeneralService.currentCruises;
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    createBooking(cruiseNumber: string): void {
        this.router.navigate(['/ferrytales/booking/create/' + cruiseNumber]);
    }
}
