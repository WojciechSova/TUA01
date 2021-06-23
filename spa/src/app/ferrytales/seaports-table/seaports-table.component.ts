import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SeaportGeneral } from '../../model/mop/SeaportGeneral';
import { SeaportGeneralService } from '../../services/mop/seaport-general.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';

@Component({
    selector: 'app-seaports-table',
    templateUrl: './seaports-table.component.html',
    styleUrls: ['./seaports-table.component.less']
})
export class SeaportsTableComponent implements OnInit {

    isConfirmationVisible = false;
    seaportDeleteAttempt = false;
    seaportCode = '';
    deleteResultMessage = 'HIDDEN';

    constructor(private router: Router,
                public seaportGeneralService: SeaportGeneralService,
                private errorHandlerService: ErrorHandlerService) {
        this.getSeaports();
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.seaportGeneralService.popup = 'hidden';
        this.router.navigate(['/']);
    }

    refresh(): void {
        this.seaportGeneralService.popup = 'hidden';
        this.deleteResultMessage = 'HIDDEN';
        this.getSeaports();
    }

    getSeaports(): void {
        this.seaportGeneralService.getSeaports().subscribe(
            (response: SeaportGeneral[]) => {
                this.seaportGeneralService.seaportsList = response;
            }
        );
    }

    listSeaports(): SeaportGeneral[] {
        return this.seaportGeneralService.seaportsList;
    }

    showAddingForm(): void {
        this.seaportGeneralService.popup = 'hidden';
        this.router.navigate(['/ferrytales/seaports/add']);
    }

    deleteSeaportClick(seaportCode: string): void {
        this.seaportGeneralService.popup = 'hidden';
        this.isConfirmationVisible = true;
        this.seaportDeleteAttempt = true;
        this.seaportCode = seaportCode;
    }

    deleteSeaport(code: string): void {
        this.deleteResultMessage = 'HIDDEN';
        this.seaportGeneralService.deleteSeaport(code).subscribe(
            () => {
                this.deleteResultMessage = 'SUCCESS';
                this.getSeaports();
            }, (error: HttpErrorResponse) => {
                this.handleDeleteSeaportError(error);
                this.getSeaports();
            });
    }

    confirmationResult(confirmationResult: boolean): void {
        this.isConfirmationVisible = false;
        if (confirmationResult) {
            if (this.seaportDeleteAttempt) {
                this.deleteSeaport(this.seaportCode);
                this.seaportCode = '';
                this.seaportDeleteAttempt = false;
            }
        }
    }

    private handleDeleteSeaportError(error: HttpErrorResponse): void {
        if (error.status === 409) {
            this.deleteResultMessage = 'IN_USE';
        } else if (error.status === 410){
            this.deleteResultMessage = 'FAILURE';
        } else {
            this.errorHandlerService.handleError(error);
        }
    }
}
