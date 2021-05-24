import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { ErrorHandler, Injectable, NgZone } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

    constructor(public router: Router,
                private zone: NgZone) {
    }

    handleError(error: HttpErrorResponse): Observable<never> {
        if (error.status === 401) {
            this.zone.run(() => this.router.navigateByUrl('/'));
            return throwError('401 Unauthorized');
        } else if (error.status === 403) {
            this.zone.run(() => this.router.navigateByUrl('/error/forbidden'));
            return throwError('403 Forbidden');
        } else if (error.status === 404) {
            this.zone.run(() => this.router.navigateByUrl('error/notfound'));
            return throwError('404 Not Found');
        } else if (error.status === 500) {
            this.zone.run(() => this.router.navigateByUrl('error/internal'));
            return throwError('500 Internal Error');
        }

        return throwError('Undefined error.');
    }
}
