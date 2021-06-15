import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { CruiseDetailsService } from '../../services/mop/cruise-details.service';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-cruise-details',
    templateUrl: './cruise-details.component.html',
    styleUrls: ['./cruise-details.component.less']
})
export class CruiseDetailsComponent implements OnInit {

    number = '';

    constructor(private router: Router,
                private route: ActivatedRoute,
                public cruiseDetailsService: CruiseDetailsService,
                public identityService: IdentityService) {
        this.number = (this.route.snapshot.paramMap.get('number') as string);
        this.getCruise();
    }

    ngOnInit(): void {
    }

    edit(cruiseNumber: string): void {
        return;
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
}
