import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FerryGeneralService } from '../../services/mop/ferry-general.service';
import { FerryGeneral } from '../../model/mop/FerryGeneral';
import { CruiseDetailsService } from '../../services/mop/cruise-details.service';
import { CruiseGeneral } from '../../model/mop/CruiseGeneral';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-add-cruise',
    templateUrl: './add-cruise.component.html',
    styleUrls: ['./add-cruise.component.less']
})
export class AddCruiseComponent implements OnInit {

    code = '';
    ferry = '';

    error = false;
    errorCode = '';

    form = new FormGroup({
        number: new FormControl('', [Validators.required, Validators.pattern('[A-Z]{6}[0-9]{6}')]),
        startDate: new FormControl('', Validators.required),
        endDate: new FormControl('', Validators.required),
        ferry: new FormControl('', Validators.required)
    });

    constructor(private route: ActivatedRoute,
                private router: Router,
                public ferryGeneralService: FerryGeneralService,
                private cruiseDetailsService: CruiseDetailsService) {
        this.code = this.route.snapshot.paramMap.get('code') as string;
        this.getFerries();
    }

    ngOnInit(): void {
    }

    getFerries(): void {
        this.ferryGeneralService.getFerries().subscribe(
            (response: FerryGeneral[]) => {
                this.ferryGeneralService.ferriesGeneralList = response;
            }
        );
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToRoutesListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/routes']);
    }

    goToRouteDetailsBreadcrumb(): void {
        this.router.navigate(['/ferrytales/routes/' + this.code]);
    }

    addCruise(cruiseNumber: string, startDate: string, endDate: string): void {
        this.error = false;

        const cruise: CruiseGeneral = {
            number: cruiseNumber,
            startDate: new Date(),
            endDate: new Date()
        };

        this.cruiseDetailsService.addCruise(cruise, this.ferry, this.code).subscribe(
            () => this.goToRouteDetailsBreadcrumb(),
            (error: HttpErrorResponse) => this.handleError(error)
        );
    }

    private handleError(error: any): void {
        this.error = true;

        if (error.status === 400) {
            this.errorCode = 'ERROR.DUPLICATE_CODE';
        }

        if (error.status === 409) {
            if (error.error === 'ERROR.FERRY_IS_BEING_USED') {
                this.errorCode = error.error;
            }
        }
    }
}
