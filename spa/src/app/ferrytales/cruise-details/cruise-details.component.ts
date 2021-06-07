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
        cruiseDetailsService.getCruise(this.number);
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    edit(cruiseNumber: string): void {
        return;
    }

    getCruise(): void {
        this.cruiseDetailsService.getCruise(this.number);
    }
}
