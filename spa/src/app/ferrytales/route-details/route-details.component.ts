import { Component, OnInit } from '@angular/core';
import { RouteDetails } from '../../model/mop/RouteDetails';
import { IdentityService } from '../../services/utils/identity.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RouteDetailsService } from '../../services/mop/route-details.service';
import {CruiseGeneralService} from '../../services/mop/cruise-general.service';

@Component({
    selector: 'app-route-details',
    templateUrl: './route-details.component.html',
    styleUrls: ['./route-details.component.less']
})
export class RouteDetailsComponent implements OnInit {

    code = '';
    cruiseDeleteResult = 'HIDDEN';

    constructor(public identityService: IdentityService,
                private route: ActivatedRoute,
                private router: Router,
                public routeDetailsService: RouteDetailsService,
                public cruiseGeneralService: CruiseGeneralService) {
        this.code = this.route.snapshot.paramMap.get('code') as string;
        this.cruiseDeleteResult = 'HIDDEN';
        this.getRoute();
    }

    ngOnInit(): void {
    }

    getRoute(): void {
        this.routeDetailsService.getRouteAndCruisesForRoute(this.code).subscribe(
            (response: RouteDetails) => {
                this.routeDetailsService.readRouteDetails(response);
            }
        );
    }

    goToHomeBreadcrumb(): void {
        this.cruiseGeneralService.popup = 'hidden';
        this.router.navigate(['/']);
    }

    goToRoutesListBreadcrumb(): void {
        this.cruiseGeneralService.popup = 'hidden';
        this.router.navigate(['/ferrytales/routes']);
    }

    addCruise(): void {
        this.cruiseGeneralService.popup = 'hidden';
        this.router.navigate(['/ferrytales/routes/' + this.code + '/cruise/add']);
    }

    showCruiseDeleteResult(result: string): void {
        this.cruiseDeleteResult = result;
        this.getRoute();
    }

    refreshClick(): void {
        this.cruiseGeneralService.popup = 'hidden';
        this.cruiseDeleteResult = 'HIDDEN';
        this.getRoute();
    }
}
