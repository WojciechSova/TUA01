import { Component, Input } from '@angular/core';
import { IdentityService } from '../../../services/utils/identity.service';
import { CabinGeneral } from '../../../model/mop/CabinGeneral';
import { Router } from '@angular/router';
import { CabinDetailsService } from '../../../services/mop/cabin-details.service';
import { FerryDetailsService } from '../../../services/mop/ferry-details.service';
import { FerryDetails } from '../../../model/mop/FerryDetails';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'app-cabin-table',
    templateUrl: './cabin-table.component.html',
    styleUrls: ['./cabin-table.component.less']
})
export class CabinTableComponent {

    cabinRemovalError = false;
    cabinGoneError = false;
    cabinRemoved = false;
    cabinToRemove = '';
    isPromptVisible = false;

    constructor(public identityService: IdentityService,
                private router: Router,
                private cabinDetailsService: CabinDetailsService,
                private ferryDetailsService: FerryDetailsService) {
    }

    @Input()
    cabins: CabinGeneral[] = [];

    @Input()
    ferry = '';

    goToCabinDetails(cabinName: string): void {
        this.router.navigate(['/ferrytales/ferries/', this.ferry, cabinName]);
    }

    displayPrompt(cabinNumber: string): void {
        this.cabinRemovalError = false;
        this.cabinGoneError = false;
        this.cabinToRemove = cabinNumber;
        this.isPromptVisible = true;
    }

    getFerry(): void {
        this.ferryDetailsService.getFerry(this.ferry).subscribe(
            (response: HttpResponse<FerryDetails>) => {
                this.ferryDetailsService.readFerryDetails(response);
            });
    }

    removeCabin(cabinNumber: string): void {
        this.cabinDetailsService.removeCabin(cabinNumber).subscribe(
            () => {
                this.getFerry();
                this.cabinRemoved = true;
            },
            (error => {
                if (error.error === 'ERROR.CABIN_USED_BY_BOOKING') {
                    this.cabinRemovalError = true;
                    this.cabinGoneError = false;
                    this.cabinRemoved = false;
                }
                else if (error.status === 410) {
                    this.getFerry();
                    this.cabinGoneError = true;
                    this.cabinRemovalError = false;
                    this.cabinRemoved = false;
                }
            })
        );
    }


    getConfirmationResult(confirmationResult: boolean): void {
        if (confirmationResult) {
            this.removeCabin(this.cabinToRemove);
        }
        this.isPromptVisible = false;
    }

    translateCabinType(cabinType: string): string {
        if (cabinType === 'First class') {
            return 'cabin-form.first';
        }
        if (cabinType === 'Second class') {
            return 'cabin-form.second';
        }
        if (cabinType === 'Third class') {
            return 'cabin-form.third';
        }
        return 'cabin-form.disabled';
    }
}
