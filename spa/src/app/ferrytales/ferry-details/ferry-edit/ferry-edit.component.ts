import { Component, EventEmitter, Output } from '@angular/core';
import { FerryDetailsService } from '../../../services/mop/ferry-details.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-ferry-edit',
    templateUrl: './ferry-edit.component.html',
    styleUrls: ['./ferry-edit.component.less']
})
export class FerryEditComponent {

    @Output()
    isEditFerryFormVisible = new EventEmitter<any>();

    constructor(public ferryDetailsService: FerryDetailsService) {
    }

    form = new FormGroup({
        onDeckCapacity: new FormControl('', [Validators.pattern('[1-9][0-9]*')]),
        vehicleCapacity: new FormControl('', [Validators.pattern('[0-9]*')])
    });

    changeFerry(): void {
        // TODO
    }

    closeComponent(): void {
        this.isEditFerryFormVisible.emit({
            ferryEdit: {
                isFormVisible: true,
                response: 'hide',
            }
        });
    }
}
