import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CabinDetailsService } from '../../services/mop/cabin-details.service';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-cabin-details',
    templateUrl: './cabin-details.component.html',
    styleUrls: ['./cabin-details.component.less']
})
export class CabinDetailsComponent implements OnInit {

    cabinNumber = '';
    ferryName = '';

    constructor(private router: Router,
                private route: ActivatedRoute,
                public cabinDetailsService: CabinDetailsService,
                public identityService: IdentityService) {
        this.cabinNumber = (this.route.snapshot.paramMap.get('cabin') as string);
        this.ferryName = (this.route.snapshot.paramMap.get('ferry') as string);
        this.getCabin();
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.cabinDetailsService.popup = 'hidden';
        this.router.navigate(['/']);
    }

    goToFerryListBreadcrumb(): void {
        this.cabinDetailsService.popup = 'hidden';
        this.router.navigate(['/ferrytales/ferries']);
    }

    goToFerryBreadcrumb(): void {
        this.cabinDetailsService.popup = 'hidden';
        this.router.navigate(['/ferrytales/ferries', this.ferryName]);
    }


    getCabin(): void {
        this.cabinDetailsService.getCabin(this.ferryName, this.cabinNumber).subscribe(
            (response) => this.cabinDetailsService.readCabinAndEtagFromResponse(response)
        );
    }

    updateCabin(): void {
        this.cabinDetailsService.popup = 'hidden';
        this.router.navigate(['/ferrytales/ferries/edit/', this.ferryName, this.cabinNumber]);
    }
}
