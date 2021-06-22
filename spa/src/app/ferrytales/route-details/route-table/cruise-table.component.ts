import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import { IdentityService } from '../../../services/utils/identity.service';
import { CruiseGeneral } from '../../../model/mop/CruiseGeneral';
import { Router } from '@angular/router';
import { CruiseDetailsService } from '../../../services/mop/cruise-details.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandlerService } from '../../../services/error-handlers/error-handler.service';

@Component({
    selector: 'app-cruise-table',
    templateUrl: './cruise-table.component.html',
    styleUrls: ['./cruise-table.component.less']
})
export class CruiseTableComponent implements OnInit {

    @Input()
    cruises: CruiseGeneral[] = [];

    @Output()
    cruiseRemoveResult = new EventEmitter<any>();

    isConfirmationVisible = false;
    private cruiseNumber = '';
    private cruiseDeleteAttempt = false;

    constructor(public identityService: IdentityService,
                private router: Router,
                private cruiseDetailsService: CruiseDetailsService,
                private errorHandlerService: ErrorHandlerService) {
        this.cruiseNumber = '';
        this.cruiseDeleteAttempt = false;
    }

    ngOnInit(): void {
    }

    cruiseDetails(cruiseNumber: string): void {
        this.router.navigate([`/ferrytales/cruises/${cruiseNumber}`]);
    }

    removeCruiseClick(cruiseNumber: string): void {
        this.isConfirmationVisible = true;
        this.cruiseDeleteAttempt = true;
        this.cruiseNumber = cruiseNumber;
    }

    confirmationResult(confirmationResult: boolean): void {
        this.isConfirmationVisible = false;
        if (confirmationResult) {
            if (this.cruiseDeleteAttempt) {
                this.deleteCruise(this.cruiseNumber);
                this.cruiseNumber = '';
                this.cruiseDeleteAttempt = false;
            }
        }
    }

    private deleteCruise(cruiseNumber: string): void {
        this.cruiseDetailsService.removeCruise(cruiseNumber).subscribe(
            () => {
                this.cruiseRemoveResult.emit('SUCCESS');
            }, (error: HttpErrorResponse) => {
                this.handleError(error);
            }
        );
    }

    private handleError(error: HttpErrorResponse): void {
        if (error.status === 410) {
            this.cruiseRemoveResult.emit('GONE');
        } else if (error.status === 409) {
            if (error.error === 'ERROR.CRUISE_ALREADY_STARTED') {
                this.cruiseRemoveResult.emit('ALREADY_STARTED');
            } else if (error.error === 'ERROR.CRUISE_IS_BEING_USED') {
                this.cruiseRemoveResult.emit('IN_USE');
            }
        } else {
            this.errorHandlerService.handleError(error);
        }
    }
}
