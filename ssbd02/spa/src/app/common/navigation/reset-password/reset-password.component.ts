import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ResetPasswordService } from '../../../services/mok/reset-password.service';

@Component({
    selector: 'app-reset-password',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.less']
})
export class ResetPasswordComponent implements OnInit {

    private resetPasswordService: ResetPasswordService;

    @Output()
    isLoginVisibleChange = new EventEmitter<boolean>();

    @Output()
    isResetPasswordVisibleChange = new EventEmitter<boolean>();
    email = '';

    form = new FormGroup({
        emailFormControl: new FormControl('', [Validators.required, Validators.email]),
    });

    constructor(resetPasswordService: ResetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.isResetPasswordVisibleChange.emit(false);
    }

    reset(): void {
        this.resetPasswordService.resetPassword(this.email).subscribe(
            () => this.closeComponent()
        );
    }

    openLogin(): void {
        this.isResetPasswordVisibleChange.emit(false);
        this.isLoginVisibleChange.emit(true);
    }
}
