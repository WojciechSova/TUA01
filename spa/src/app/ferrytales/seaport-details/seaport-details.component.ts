import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IdentityService } from '../../services/utils/identity.service';
import { SeaportDetailsService } from '../../services/mop/seaport-details.service';

@Component({
    selector: 'app-seaport',
    templateUrl: './seaport-details.component.html',
    styleUrls: ['./seaport-details.component.less']
})
export class SeaportDetailsComponent implements OnInit {

    readonly HIDDEN = 'hide';
    readonly SUCCESS = 'success';
    readonly GONE = 'gone';
    readonly FAILURE = 'failure';
    readonly OPTIMISTIC_LOCK = 'optimisticLock';
    readonly NAME_NOT_UNIQUE = 'nameNotUnique';

    seaportEdit = {
        isFormVisible: false,
        response: 'hide'
    };

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
        this.seaportDetailsService.getSeaportByCode(this.code).subscribe(response => {
                this.seaportDetailsService.readSeaportDetails(response);
            }
        );
    }

    refreshButton(): void {
        this.seaportEdit.response = this.HIDDEN;
        this.getSeaport();
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToSeaportListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/seaports']);
    }

    changeEditSeaportFormVisible(seaportEdit: any): void {
        this.seaportEdit = seaportEdit;
        this.getSeaport();
    }
}
