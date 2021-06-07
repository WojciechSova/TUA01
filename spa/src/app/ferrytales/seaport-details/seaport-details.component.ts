import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeaportDetails } from '../../model/mop/SeaportDetails';
import { IdentityService } from '../../services/utils/identity.service';
import { SeaportDetailsService } from '../../services/mop/seaport-details.service';

@Component({
    selector: 'app-seaport',
    templateUrl: './seaport-details.component.html',
    styleUrls: ['./seaport-details.component.less']
})
export class SeaportDetailsComponent implements OnInit {

    code = '';

    constructor(private route: ActivatedRoute,
                private router: Router,
                public identityService: IdentityService,
                public seaportDetailsService: SeaportDetailsService) {
        this.code = this.route.snapshot.paramMap.get('code') as string;
        this.getSeaport();
    }

    ngOnInit(): void {
    }

    getSeaport(): void {
        this.seaportDetailsService.getSeaportByCode(this.code).subscribe(
            (response: SeaportDetails) => {
                this.seaportDetailsService.readSeaportDetails(response);
            }
        );
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToSeaportListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/seaports']);
    }
}
