import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CruiseDetailsService } from '../../services/mop/cruise-details.service';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-cruise-details',
    templateUrl: './cruise-details.component.html',
    styleUrls: ['./cruise-details.component.less']
})
export class CruiseDetailsComponent implements OnInit {

    readonly HIDDEN = 'hide';
    readonly SUCCESS = 'success';
    readonly GONE = 'gone';
    readonly FAILURE = 'failure';
    readonly OPTIMISTIC_LOCK = 'optimisticLock';
    readonly FERRY_BEING_USED = 'ferryBeingUsed';

    number = '';

    actualDate = new Date();

    cruiseEdit = {
        isFormVisible: false,
        response: 'hide'
    };

    constructor(private router: Router,
                private route: ActivatedRoute,
                public cruiseDetailsService: CruiseDetailsService,
                public identityService: IdentityService) {
        this.number = (this.route.snapshot.paramMap.get('number') as string);
        this.actualDate = new Date();
        this.actualDate.setUTCHours(this.actualDate.getUTCHours() + parseInt(this.identityService.getTimezone(), 10));

        this.getCruise();
    }

    ngOnInit(): void {
    }

    getCruise(): void {
        this.cruiseDetailsService.getCruise(this.number).subscribe(
            (response) => this.cruiseDetailsService.readCruiseAndEtagFromResponse(response)
        );
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToRoutesListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/routes']);
    }

    goToRouteBreadcrumb(): void {
        this.router.navigate([`ferrytales/routes/${this.cruiseDetailsService.cruise.route.code}`]);
    }

    changeEditCruiseFormVisible(cruiseEdit: any): void {
        this.cruiseEdit = cruiseEdit;
        this.getCruise();
    }
}
