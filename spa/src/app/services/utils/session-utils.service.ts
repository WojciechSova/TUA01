import { EventEmitter, Injectable, Output } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class SessionUtilsService {

    @Output()
    isSessionTimeoutVisibleChange = new EventEmitter<boolean>();

    private timeout: number | undefined;

    constructor() {
    }

    public setSessionTimeout(expTimeInSec: number): void {
        this.clearSessionTimeout();
        const time = expTimeInSec * 1000 - new Date().getTime();
        this.timeout = setTimeout(() => this.isSessionTimeoutVisibleChange.emit(true), time);
    }

    public clearSessionTimeout(): void {
        if (this.timeout) {
            clearTimeout(this.timeout);
            this.timeout = undefined;
        }
    }
}
