import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FerryGeneral } from '../../model/mop/FerryGeneral';
import { FerryGeneralService } from '../../services/mop/ferry-general.service';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';

@Component({
    selector: 'app-ferries-table',
    templateUrl: './ferries-table.component.html',
    styleUrls: ['./ferries-table.component.less']
})
export class FerriesTableComponent implements OnInit {

    public ferryUsed = false;
    isPromptVisible = false;
    private ferryNameToRemove = '';

    constructor(private router: Router,
                private ferryGeneralService: FerryGeneralService,
                private errorHandlerService: ErrorHandlerService) {
        this.getFerries();
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    getFerries(): void {
        this.ferryUsed = false;
        this.ferryGeneralService.getFerries().subscribe(
            (response: FerryGeneral[]) => {
                this.ferryGeneralService.ferriesGeneralList = response;
            }
        );
    }

    listFerries(): FerryGeneral[] {
        return this.ferryGeneralService.ferriesGeneralList;
    }

    goToAddFerryForm(): void {
        this.router.navigate(['/ferrytales/ferries/add']);
    }

    goToFerryDetails(name: string): void {
        this.router.navigate(['/ferrytales/ferries/', name]);
    }

    removeFerry(ferryName: string): void {
        this.ferryGeneralService.remove(ferryName).subscribe(
            () => this.getFerries(),
            (error => {
                if (error.error === 'ERROR.FERRY_IS_BEING_USED') {
                    this.ferryUsed = true;
                } else {
                    this.errorHandlerService.handleError(error);
                }
            })
        );
    }

    getConfirmationResult(confirmationResult: any): void {
        if (confirmationResult) {
            this.removeFerry(this.ferryNameToRemove);
        }
        this.isPromptVisible = false;
    }

    displayPrompt(ferryName: string): void {
        this.ferryUsed = false;
        this.ferryNameToRemove = ferryName;
        this.isPromptVisible = true;
    }
}
