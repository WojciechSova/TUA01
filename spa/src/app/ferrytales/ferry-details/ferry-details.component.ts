import { Component, OnInit } from '@angular/core';
import { IdentityService } from '../../services/utils/identity.service';
import { FerryDetailsService } from '../../services/mop/ferry-details.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-ferry-details',
    templateUrl: './ferry-details.component.html',
    styleUrls: ['./ferry-details.component.less']
})
export class FerryDetailsComponent implements OnInit {

    readonly HIDDEN = 'hide';
    readonly SUCCESS = 'success';
    readonly GONE = 'gone';
    readonly FAILURE = 'failure';
    readonly OPTIMISTIC_LOCK = 'optimisticLock';

    ferryEdit = {
        isFormVisible: false,
        response: 'hide'
    };

    name = '';

    constructor(public identityService: IdentityService,
                public ferryDetailsService: FerryDetailsService,
                private router: Router,
                private route: ActivatedRoute) {
        this.name = this.route.snapshot.paramMap.get('name') as string;
        this.getFerry(true);
    }

    ngOnInit(): void {
    }

    getFerry(hideResponse: boolean): void {
        if (hideResponse) {
            this.ferryEdit.response = 'hide';
        }
        this.ferryDetailsService.getFerry(this.name).subscribe(response => {
            this.ferryDetailsService.readFerryDetails(response);
        });
    }

    goToHomeBreadcrumb(): void {
        this.ferryDetailsService.popup = 'hidden';
        this.router.navigate(['/']);
    }

    goToFerryListBreadcrumb(): void {
        this.ferryDetailsService.popup = 'hidden';
        this.router.navigate(['/ferrytales/ferries']);
    }

    addCabin(): void {
        this.ferryDetailsService.popup = 'hidden';
        this.router.navigate(['ferrytales/ferries/' + this.name + '/addCabin']);
    }

    changeEditFerryFormVisible(seaportEdit: any): void {
        this.ferryDetailsService.popup = 'hidden';
        this.ferryEdit = seaportEdit;
    }
}
