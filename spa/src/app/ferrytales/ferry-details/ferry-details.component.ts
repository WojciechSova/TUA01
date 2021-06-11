import { Component, OnInit } from '@angular/core';
import { FerryDetails } from '../../model/mop/FerryDetails';
import { IdentityService } from '../../services/utils/identity.service';
import { FerryDetailsService } from '../../services/mop/ferry-details.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-ferry-details',
    templateUrl: './ferry-details.component.html',
    styleUrls: ['./ferry-details.component.less']
})
export class FerryDetailsComponent implements OnInit {

    name = '';

    constructor(public identityService: IdentityService,
                public ferryDetailsService: FerryDetailsService,
                private router: Router,
                private route: ActivatedRoute) {
        this.name = this.route.snapshot.paramMap.get('name') as string;
        this.getFerry();
    }

    ngOnInit(): void {
    }

    getFerry(): void {
        this.ferryDetailsService.getFerry(this.name).subscribe(
            (response: FerryDetails) => {
                this.ferryDetailsService.readFerryDetails(response);
            });
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerryListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/ferries']);
    }

    addCabin(): void {
        this.router.navigate(['ferrytales/ferries/' + this.name + '/addCabin']);
    }
}
