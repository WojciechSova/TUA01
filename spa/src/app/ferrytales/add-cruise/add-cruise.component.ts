import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FerryGeneralService } from '../../services/mop/ferry-general.service';
import { FerryGeneral } from '../../model/mop/FerryGeneral';
import { CruiseDetailsService } from '../../services/mop/cruise-details.service';
import { CruiseGeneral } from '../../model/mop/CruiseGeneral';
import { HttpErrorResponse } from '@angular/common/http';
import { IAngularMyDpOptions, IMyDateModel } from 'angular-mydatepicker';
import { IdentityService } from '../../services/utils/identity.service';
import {CruiseGeneralService} from '../../services/mop/cruise-general.service';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';

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

    startHour = 0;
    startMinute = 0;

    endHour = 0;
    endMinute = 0;

    form = new FormGroup({
        number: new FormControl('', [Validators.required, Validators.pattern('[A-Z]{6}[0-9]{6}')]),
        startDate: new FormControl('', Validators.required),
        startHour: new FormControl('', Validators.required),
        startMinute: new FormControl('', Validators.required),
        endDate: new FormControl('', Validators.required),
        endHour: new FormControl('', Validators.required),
        endMinute: new FormControl('', Validators.required),
        ferry: new FormControl('', Validators.required)
    });

    constructor(private route: ActivatedRoute,
                private router: Router,
                public ferryGeneralService: FerryGeneralService,
                private cruiseDetailsService: CruiseDetailsService,
                private identityService: IdentityService,
                private errorHandlerService: ErrorHandlerService,
                private cruiseGeneralService: CruiseGeneralService) {
        this.code = this.route.snapshot.paramMap.get('code') as string;
        this.getFerries();
    }

    actualDate: Date | undefined;

    myDpOptions: IAngularMyDpOptions = {};

    myDateInit = false;
    startDate: IMyDateModel = { isRange: false };
    endDate: IMyDateModel = { isRange: false };

    ngOnInit(): void {
        this.myDpOptions = {
            dateRange: false,
            dateFormat: 'dd.mm.yyyy'
        };

        this.startDate = { isRange: false, singleDate: { jsDate: this.actualDate } };
        this.endDate = { isRange: false, singleDate: { jsDate: this.actualDate } };
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

    addCruise(cruiseNumber: string): void {
        this.error = false;

        this.startDate.singleDate?.jsDate?.setUTCHours(this.startHour - parseInt(this.identityService.getTimezone(), 10), this.startMinute);
        this.startDate.singleDate?.jsDate?.setDate(this.startDate.singleDate?.jsDate?.getDate() + 1);

        this.endDate.singleDate?.jsDate?.setUTCHours(this.endHour - parseInt(this.identityService.getTimezone(), 10), this.endMinute);
        this.endDate.singleDate?.jsDate?.setDate(this.endDate.singleDate?.jsDate?.getDate() + 1);

        const cruise: CruiseGeneral = {
            number: cruiseNumber,
            startDate: this.startDate.singleDate?.jsDate ? this.startDate.singleDate?.jsDate.toJSON() : new Date(0).toJSON(),
            endDate: this.endDate.singleDate?.jsDate ? this.endDate.singleDate?.jsDate.toJSON() : new Date(0).toJSON()
        };

        this.cruiseGeneralService.popup = 'hidden';
        this.cruiseDetailsService.addCruise(cruise, this.ferry, this.code).subscribe(
            () => {
                this.goToRouteDetailsBreadcrumb();
                this.cruiseGeneralService.popup = 'add_cruise_success';
                setTimeout(() => this.cruiseGeneralService.popup = 'hidden', 5000);
            },
            (error: HttpErrorResponse) => this.handleError(error)
        );
    }

    private handleError(error: any): void {
        this.error = true;

        if (error.status === 400) {
            this.errorCode = 'ERROR.BAD_REQUEST';
        } else if (error.status === 409) {
            if (error.error === 'ERROR.CRUISE_NUMBER_UNIQUE') {
                this.errorCode = error.error;
            } else if (error.error === 'ERROR.FERRY_IS_BEING_USED') {
                this.errorCode = error.error;
            }
        } else {
            this.errorHandlerService.handleError(error);
        }
    }

    range(start: number, end: number): number[] {
        const ans = [];

        for (let i = start; i <= end; i++) {
            ans.push(i);
        }

        return ans;
    }
}
