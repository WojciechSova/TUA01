import { Component, EventEmitter, Output } from '@angular/core';
import { SeaportDetailsService } from '../../../services/mop/seaport-details.service';

@Component({
    selector: 'app-seaport-edit',
    templateUrl: './seaport-edit.component.html',
    styleUrls: ['./seaport-edit.component.less']
})
export class SeaportEditComponent {

    @Output()
    isEditSeaportFormVisible = new EventEmitter<boolean>();

    constructor(private seaportDetailsService: SeaportDetailsService) {
    }

    changeSeaport(): void {
        // TODO
    }

    closeComponent(): void {
        this.isEditSeaportFormVisible.emit(false);
    }
}
