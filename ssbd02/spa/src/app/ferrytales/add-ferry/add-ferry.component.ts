import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FerryGeneralService } from '../../services/mop/ferry-general.service';
import { FerryGeneral } from '../../model/mop/FerryGeneral';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';

@Component({
    selector: 'app-add-ferry',
    templateUrl: './add-ferry.component.html',
    styleUrls: ['./add-ferry.component.less']
})
export class AddFerryComponent implements OnInit {

    error = false;

    constructor(private router: Router,
                private ferryGeneralService: FerryGeneralService,
                private errorHandlerService: ErrorHandlerService) {
    }

    form = new FormGroup({
        name: new FormControl('', [Validators.required, Validators.maxLength(30)]),
        vehicleCapacity: new FormControl('', [Validators.required, Validators.min(0)]),
        onDeckCapacity: new FormControl('', [Validators.required, Validators.min(1)])
    });

    ngOnInit(): void {
    }

    addFerry(newName: string, newVehicleCapacity: number, newOnDeckCapacity: number): void {
        const ferry: FerryGeneral = {
            name: newName,
            vehicleCapacity: newVehicleCapacity,
            onDeckCapacity: newOnDeckCapacity
        };

        this.ferryGeneralService.popup = 'hidden';
        this.ferryGeneralService.addFerry(ferry).subscribe(
            () => {
                this.goToFerryListBreadcrumb();
                this.ferryGeneralService.popup = 'add_ferry_success';
                setTimeout(() => this.ferryGeneralService.popup = 'hidden', 5000);
            },
            (error: any) => {
                if (error.status === 409) {
                    this.error = true;
                } else {
                    this.errorHandlerService.handleError(error);
                }
            });
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerryListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/ferries']);
    }
}
