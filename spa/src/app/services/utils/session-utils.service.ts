import { EventEmitter, Injectable, Output } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class SessionUtilsService {

    @Output()
    isSessionTimeoutVisibleChange = new EventEmitter<boolean>();

    @Output()
    isSessionNearlyTimeoutVisibleChange = new EventEmitter<boolean>();

    private timeout: number | undefined;
    private nearlyTimeout: number | undefined;
    public timeoutVisible: boolean = false;
    public nearlyTimeoutVisible: boolean = false;

    constructor() {
    }

    public setSessionTimeout(expTimeInSec: number): void {
        this.clearSessionTimeout();
        const time = expTimeInSec * 1000 - new Date().getTime();
        this.timeout = setTimeout(() => {
            this.isSessionTimeoutVisibleChange.emit(true);
            this.isSessionNearlyTimeoutVisibleChange.emit(false);
        }, time);
    }

    public setSessionNearlyTimeout(expTimeInSec: number): void {
        this.clearSessionNearlyTimeout();
        const time = 0.9 * (expTimeInSec * 1000 - new Date().getTime());
        this.nearlyTimeout = setTimeout(() => this.isSessionNearlyTimeoutVisibleChange.emit(true), time);
    }

    public clearSessionTimeout(): void {
        if (this.timeout) {
            clearTimeout(this.timeout);
            this.timeout = undefined;
        }
    }

    public clearSessionNearlyTimeout(): void {
        if (this.nearlyTimeout) {
            clearTimeout(this.nearlyTimeout);
            this.nearlyTimeout = undefined;
        }
    }
}
