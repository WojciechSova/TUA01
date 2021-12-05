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
    result = 'hidden';

    constructor(private router: Router,
                public ferryGeneralService: FerryGeneralService,
                private errorHandlerService: ErrorHandlerService) {
        this.getFerries();
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.ferryGeneralService.popup = 'hidden';
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
        this.ferryGeneralService.popup = 'hidden';
        this.router.navigate(['/ferrytales/ferries/add']);
    }

    goToFerryDetails(name: string): void {
        this.ferryGeneralService.popup = 'hidden';
        this.router.navigate(['/ferrytales/ferries/', name]);
    }

    removeFerry(ferryName: string): void {
        this.result = 'hidden';
        this.ferryGeneralService.popup = 'hidden';
        this.ferryGeneralService.remove(ferryName).subscribe(
            () => {
                this.getFerries();
                this.result = 'success';
                setTimeout(() => this.result = 'hidden', 5000);
            },
            (error => {
                if (error.error === 'ERROR.FERRY_IS_BEING_USED') {
                    this.ferryUsed = true;
                } else if (error.status === 410) {
                    this.result = 'gone';
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
