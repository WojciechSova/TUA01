import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-reset-password',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.less']
})
export class ResetPasswordComponent implements OnInit {


    @Output()
    isLoginVisibleChange = new EventEmitter<boolean>();

    @Output()
    isResetPasswordVisibleChange = new EventEmitter<boolean>();
    email = '';

    form = new FormGroup({
        emailFormControl: new FormControl('', [Validators.required, Validators.email]),
    });

    constructor() {
    }

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.isResetPasswordVisibleChange.emit(false);
    }

    reset(): void {
        return;
    }

    openLogin(): void {
        this.isResetPasswordVisibleChange.emit(false);
        this.isLoginVisibleChange.emit(true);
    }
}
