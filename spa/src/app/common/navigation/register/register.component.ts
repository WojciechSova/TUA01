import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.less']
})
export class RegisterComponent implements OnInit {

    constructor() {
    }

    @Output()
    isLoginVisibleChange = new EventEmitter<boolean>();

    @Output()
    isRegisterVisibleChange = new EventEmitter<boolean>();

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.isLoginVisibleChange.emit(false);
        this.isRegisterVisibleChange.emit(false);
    }

    openLogin(): void {
        this.isLoginVisibleChange.emit(true);
        this.isRegisterVisibleChange.emit(false);
    }
}
