import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ErrorHandler, Injectable, NgZone } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

    constructor(public router: Router,
                private zone: NgZone) {
    }

    handleError(error: HttpErrorResponse): void {
        if (error.status === 401) {
            this.zone.run(() => this.router.navigateByUrl('/error/unauthorized'));
        } else if (error.status === 403) {
            this.zone.run(() => this.router.navigateByUrl('/error/forbidden'));
        } else if (error.status === 404) {
            this.zone.run(() => this.router.navigateByUrl('error/notfound'));
        } else if (error.status === 410) {
            this.zone.run(() => this.router.navigateByUrl('error/gone'));
        } else if (error.status.toString().match('4|5..')) {
            this.zone.run(() => this.router.navigateByUrl('error/internal'));
        }
    }
}
