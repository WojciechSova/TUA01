import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IAngularMyDpOptions, IMyDateModel } from 'angular-mydatepicker';
import { CruiseDetailsService } from '../../../services/mop/cruise-details.service';
import { IdentityService } from '../../../services/utils/identity.service';

@Component({
    selector: 'app-edit-cruise',
    templateUrl: './edit-cruise.component.html',
    styleUrls: ['./edit-cruise.component.less']
})
export class EditCruiseComponent implements OnInit, OnChanges {

    @Output()
    isEditCruiseFormVisible = new EventEmitter<any>();

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
                private identityService: IdentityService) {
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
        this.beginStartDate
            .setUTCHours(this.beginStartDate.getUTCHours() + parseInt(this.identityService.getTimezone(), 10));
        this.startDate = { isRange: false, singleDate: { jsDate: this.beginStartDate } };
        this.startHour = this.beginStartDate.getUTCHours();
        this.startMinute = this.beginStartDate.getMinutes();

        this.beginEndDate
            .setUTCHours(this.beginEndDate.getUTCHours() + parseInt(this.identityService.getTimezone(), 10));
        this.endDate = { isRange: false, singleDate: { jsDate: this.beginEndDate } };
        this.endHour = this.beginEndDate.getUTCHours();
        this.endMinute = this.beginEndDate.getMinutes();
    }
}
