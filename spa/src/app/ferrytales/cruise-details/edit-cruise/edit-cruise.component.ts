import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-edit-cruise',
    templateUrl: './edit-cruise.component.html',
    styleUrls: ['./edit-cruise.component.less']
})
export class EditCruiseComponent implements OnInit {

    @Output()
    isEditCruiseFormVisible = new EventEmitter<any>();

    form = new FormGroup({
        // number: new FormControl('', [Validators.required, Validators.pattern('[A-Z]{6}[0-9]{6}')]),
        startDate: new FormControl('', Validators.required),
        startHour: new FormControl('', Validators.required),
        startMinute: new FormControl('', Validators.required),
        endDate: new FormControl('', Validators.required),
        endHour: new FormControl('', Validators.required),
        endMinute: new FormControl('', Validators.required),
        // ferry: new FormControl('', Validators.required)
    });

    startHour = 0;
    startMinute = 0;

    endHour = 0;
    endMinute = 0;

    actualDate: Date | undefined;

    // myDpOptions: IAngularMyDpOptions = {};
    //
    // startDate: IMyDateModel = { isRange: false };
    // endDate: IMyDateModel = { isRange: false };

    constructor() {
    }

    ngOnInit(): void {
        // this.myDpOptions = {
        //     dateRange: false,
        //     dateFormat: 'dd.mm.yyyy'
        // };
        //
        // this.startDate = { isRange: false, singleDate: { jsDate: this.actualDate } };
        // this.endDate = { isRange: false, singleDate: { jsDate: this.actualDate } };
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
}
