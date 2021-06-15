import { Component, EventEmitter, Output } from '@angular/core';
import { SeaportDetailsService } from '../../../services/mop/seaport-details.service';

@Component({
    selector: 'app-seaport-edit',
    templateUrl: './seaport-edit.component.html',
    styleUrls: ['./seaport-edit.component.less']
})
export class SeaportEditComponent {

    readonly HIDDEN = 'hide';
    readonly SUCCESS = 'success';
    readonly GONE = 'gone';
    readonly FAILURE = 'failure';
    readonly OPTIMISTIC_LOCK = 'optimisticLock';
    readonly NAME_NOT_UNIQUE = 'nameNotUnique';

    @Output()
    isEditSeaportFormVisible = new EventEmitter<any>();

    constructor(private seaportDetailsService: SeaportDetailsService) {
    }

    changeSeaport(city: string): void {
        this.seaportDetailsService.seaport.city = city;
        this.seaportDetailsService.updateSeaport(this.seaportDetailsService.seaport)
            .subscribe(() => {
                    this.emit(this.SUCCESS);
                },
                (error) => {
                    this.handleError(error);
                });
    }

    closeComponent(): void {
        this.emit(this.HIDDEN);
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
                this.emit(this.FAILURE);
            }
        } else {
            this.emit(this.FAILURE);
        }
    }
}
