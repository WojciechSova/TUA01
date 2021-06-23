import { Component, EventEmitter, OnInit, Output} from '@angular/core';
import { SeaportDetailsService } from '../../../services/mop/seaport-details.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ErrorHandlerService } from '../../../services/error-handlers/error-handler.service';

@Component({
    selector: 'app-seaport-edit',
    templateUrl: './seaport-edit.component.html',
    styleUrls: ['./seaport-edit.component.less']
})
export class SeaportEditComponent implements OnInit {

    readonly HIDDEN = 'hide';
    readonly SUCCESS = 'success';
    readonly GONE = 'gone';
    readonly FAILURE = 'failure';
    readonly OPTIMISTIC_LOCK = 'optimisticLock';
    readonly NAME_NOT_UNIQUE = 'nameNotUnique';

    isConfirmationVisible = false;
    editCityName = '';

    form = new FormGroup({
        cityName: new FormControl('', [Validators.required, Validators.maxLength(30)])
    });

    @Output()
    isEditSeaportFormVisible = new EventEmitter<any>();

    constructor(private seaportDetailsService: SeaportDetailsService,
                private errorHandlerService: ErrorHandlerService) {
    }

    changeSeaportConfirmClick(city: string): void {
        this.isConfirmationVisible = true;
        this.editCityName = city;
    }

    closeComponent(): void {
        this.emit(this.HIDDEN);
    }

    confirmationResult(confirmationResult: boolean): void {
        this.isConfirmationVisible = false;
        if (confirmationResult) {
            this.changeSeaport(this.editCityName);
            this.editCityName = '';
        }
    }

    private changeSeaport(city: string): void {
        this.seaportDetailsService.seaport.city = city;
        this.seaportDetailsService.updateSeaport(this.seaportDetailsService.seaport)
            .subscribe(() => {
                    this.emit(this.SUCCESS);
                },
                (error) => {
                    this.handleError(error);
                });
    }

    private emit(response: string): void {
        this.isEditSeaportFormVisible.emit({
                isFormVisible: false,
                response
        });
    }

    private handleError(error: any): void {
        if (error.status === 410) {
            this.emit(this.GONE);
        } else if (error.status === 409) {
            if (error.error === 'ERROR.OPTIMISTIC_LOCK') {
                this.emit(this.OPTIMISTIC_LOCK);
            } else if (error.error === 'ERROR.SEAPORT_CITY_UNIQUE') {
                this.emit(this.NAME_NOT_UNIQUE);
            } else {
                this.errorHandlerService.handleError(error);
            }
        } else {
            this.errorHandlerService.handleError(error);
        }
    }

    ngOnInit(): void {

    }
}
