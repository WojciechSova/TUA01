import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FerryDetailsService } from '../../../services/mop/ferry-details.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-ferry-edit',
    templateUrl: './ferry-edit.component.html',
    styleUrls: ['./ferry-edit.component.less']
})
export class FerryEditComponent {

    isConfirmationVisible = false;

    readonly HIDDEN = 'hide';
    readonly SUCCESS = 'success';
    readonly GONE = 'gone';
    readonly FAILURE = 'failure';
    readonly OPTIMISTIC_LOCK = 'optimisticLock';

    @Output()
    isEditFerryFormVisible = new EventEmitter<any>();

    onDeckCapacity: string | undefined;
    vehicleCapacity: string | undefined;

    @Input()
    ferryName = '';

    editFailed = false;

    constructor(public ferryDetailsService: FerryDetailsService) {
    }

    form = new FormGroup({
        onDeckCapacity: new FormControl('', [Validators.pattern('[1-9][0-9]*')]),
        vehicleCapacity: new FormControl('', [Validators.pattern('[0-9]*')])
    });

    changeFerryClick(): void {
        this.isConfirmationVisible = true;
    }

    changeFerry(): void {
        this.editFailed = false;
        if (this.onDeckCapacity !== undefined) {
            this.ferryDetailsService.ferry.onDeckCapacity = this.onDeckCapacity;
        }
        if (this.vehicleCapacity !== undefined) {
            this.ferryDetailsService.ferry.vehicleCapacity = this.vehicleCapacity;
        }
        this.ferryDetailsService.updateFerry(this.ferryDetailsService.ferry).subscribe(
            () => {
                this.emit(this.SUCCESS);
            }, (error) => {
                this.handleError(error);
            }
        );
    }

    private emit(response: string): void {
        this.ferryDetailsService.getFerry(this.ferryDetailsService.ferry.name);
        this.isEditFerryFormVisible.emit({
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
            } else {
                this.emit(this.FAILURE);
            }
        } else {
            this.emit(this.FAILURE);
        }
    }

    closeComponent(): void {
        this.emit(this.HIDDEN);
    }

    confirmationResult(confirmationResult: boolean): void {
        this.isConfirmationVisible = false;
        if (confirmationResult) {
            this.changeFerry();
        }
    }
}
