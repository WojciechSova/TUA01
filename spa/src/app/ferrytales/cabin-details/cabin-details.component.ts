import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CabinDetailsService } from '../../services/mop/cabin-details.service';
import { IdentityService } from '../../services/utils/identity.service';
import { CabinDetails } from "../../model/mop/CabinDetails";

@Component({
    selector: 'app-cabin-details',
    templateUrl: './cabin-details.component.html',
    styleUrls: ['./cabin-details.component.less']
})
export class CabinDetailsComponent implements OnInit {

    cabin: CabinDetails;

    constructor(private router: Router,
                public cabinDetailsService: CabinDetailsService,
                public identityService: IdentityService) {
        this.cabin = cabinDetailsService.getCabin("123");
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    getCabin(): void {
        this.cabinDetailsService.getCabin("123");
    }
}
