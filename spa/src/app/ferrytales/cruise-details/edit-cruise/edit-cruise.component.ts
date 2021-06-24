import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IAngularMyDpOptions, IMyDateModel } from 'angular-mydatepicker';
import { CruiseDetailsService } from '../../../services/mop/cruise-details.service';
import { IdentityService } from '../../../services/utils/identity.service';
import { CruiseGeneral } from '../../../model/mop/CruiseGeneral';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandlerService } from '../../../services/error-handlers/error-handler.service';

@Component({
    selector: 'app-edit-cruise',
    templateUrl: './edit-cruise.component.html',
    styleUrls: ['./edit-cruise.component.less']
})
export class EditCruiseComponent implements OnInit, OnChanges {

    isPromptVisible = false;

    readonly HIDDEN = 'hide';
    readonly SUCCESS = 'success';
    readonly GONE = 'gone';
    readonly FAILURE = 'failure';
    readonly OPTIMISTIC_LOCK = 'optimisticLock';
    readonly FERRY_BEING_USED = 'ferryBeingUsed';

    @Output()
    isEditCruiseFormVisible = new EventEmitter<any>();

    @Input()
    cruiseNumber = '';

    @Input()
    beginStartDate: Date = new Date();

    @Input()
    beginEndDate: Date = new Date();

    form = new FormGroup({
        startDate: new FormControl('', Validators.required),
        startHour: new FormControl('', Validators.required),
        startMinute: new FormControl('', Validators.required),
        endDate: new FormControl('', Validators.required),
        endHour: new FormControl('', Validators.required),
        endMinute: new FormControl('', Validators.required),
    });

    startHour = 0;
    startMinute = 0;

    endHour = 0;
    endMinute = 0;

    actualDate: Date | undefined;

    myDpOptions: IAngularMyDpOptions = {};

    startDate: IMyDateModel = { isRange: false };
    endDate: IMyDateModel = { isRange: false };

    constructor(private cruiseDetailsService: CruiseDetailsService,
                private identityService: IdentityService,
                private errorHandlerService: ErrorHandlerService) {
        this.startDate = { isRange: false, singleDate: { jsDate: this.beginStartDate } };
        this.endDate = { isRange: false, singleDate: { jsDate: this.beginEndDate } };
    }

    ngOnInit(): void {
        this.myDpOptions = {
            dateRange: false,
            dateFormat: 'dd.mm.yyyy'
        };
    }

    changeCruise(): void {
        this.startDate.singleDate?.jsDate?.setUTCHours(this.startHour - parseInt(this.identityService.getTimezone(), 10), this.startMinute);
        this.startDate.singleDate?.jsDate?.setDate(this.startDate.singleDate?.jsDate?.getDate() + 1);

        this.endDate.singleDate?.jsDate?.setUTCHours(this.endHour - parseInt(this.identityService.getTimezone(), 10), this.endMinute);
        this.endDate.singleDate?.jsDate?.setDate(this.endDate.singleDate?.jsDate?.getDate() + 1);

        const cruise: CruiseGeneral = {
            number: this.cruiseDetailsService.cruise.number,
            startDate: this.startDate.singleDate?.jsDate ? this.startDate.singleDate?.jsDate.toJSON() : new Date(0).toJSON(),
            endDate: this.endDate.singleDate?.jsDate ? this.endDate.singleDate?.jsDate.toJSON() : new Date(0).toJSON(),
            version: this.cruiseDetailsService.cruise.version
        };

        this.cruiseDetailsService.updateCruise(cruise).subscribe(
            () => this.emit(this.SUCCESS),
            (error: HttpErrorResponse) => this.handleError(error)
        );
    }

    private handleError(error: any): void {
        if (error.status === 400) {
            this.emit(this.FAILURE);
        } else if (error.status === 410) {
            this.emit(this.GONE);
        } else if (error.status === 409) {
            if (error.error === 'ERROR.OPTIMISTIC_LOCK') {
                this.emit(this.OPTIMISTIC_LOCK);
            } else if (error.error === 'ERROR.FERRY_IS_BEING_USED') {
                this.emit(this.FERRY_BEING_USED);
            } else {
                this.errorHandlerService.handleError(error);
            }
        } else {
            this.errorHandlerService.handleError(error);
        }
    }

    private emit(response: string): void {
        this.isEditCruiseFormVisible.emit({
            isFormVisible: false,
            response
        });
    }

    closeComponent(): void {
        this.emit('');
    }

    range(start: number, end: number): number[] {
        const ans = [];

        for (let i = start; i <= end; i++) {
            ans.push(i);
        }

        return ans;
    }

    ngOnChanges(changes: SimpleChanges): void {
        const offset = new Date().getTimezoneOffset() / 60;

        this.beginStartDate
            .setUTCHours(this.beginStartDate.getUTCHours() + parseInt(this.identityService.getTimezone(), 10) + offset);
        this.startDate = { isRange: false, singleDate: { jsDate: this.beginStartDate } };
        this.startHour = this.beginStartDate.getUTCHours() - offset;

        if (this.startHour < 0) {
            this.startHour = 24 - this.startHour;
        } else if (this.startHour > 23) {
            this.startHour = this.startHour - 24;
        }
        this.startMinute = this.beginStartDate.getMinutes();

        this.beginEndDate
            .setUTCHours(this.beginEndDate.getUTCHours() + parseInt(this.identityService.getTimezone(), 10) + offset);
        this.endDate = { isRange: false, singleDate: { jsDate: this.beginEndDate } };
        this.endHour = this.beginEndDate.getUTCHours() - offset;

        if (this.endHour < 0) {
            this.endHour = 24 - this.endHour;
        } else if (this.endHour > 23) {
            this.endHour = this.endHour - 24;
        }
        this.endMinute = this.beginEndDate.getMinutes();
    }

    displayPrompt(): void {
        this.isPromptVisible = true;
    }

    getConfirmationResult(event: boolean): void {
        if (event) {
            this.changeCruise();
        }

        this.isPromptVisible = false;
    }
}
