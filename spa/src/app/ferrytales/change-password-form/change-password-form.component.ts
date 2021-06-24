import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validatePassword } from '../../common/navigation/register/matching.validator';
import { ChangePasswordService } from '../../services/mok/change-password.service';
import { HttpErrorResponse } from '@angular/common/http';
import { AccountDetailsService } from '../../services/mok/account-details.service';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';

@Component({
    selector: 'app-change-password-form',
    templateUrl: './change-password-form.component.html',
    styleUrls: ['./change-password-form.component.less']
})
export class ChangePasswordFormComponent {

    public samePassword = false;
    public incorrectPassword = false;

    constructor(private changePasswordService: ChangePasswordService,
                private errorHandlerService: ErrorHandlerService,
                private accountDetailsService: AccountDetailsService) {
    }

    @Output()
    isChangePasswordFormVisibleChange = new EventEmitter<boolean>();

    form = new FormGroup({
        oldPassword: new FormControl('', [Validators.required, validatePassword]),
        password: new FormControl('', [Validators.required, Validators.minLength(8), validatePassword]),
        passwordRepeat: new FormControl('', [Validators.required, validatePassword]),
    });

    closeComponent(): void {
        this.isChangePasswordFormVisibleChange.emit(false);
        this.samePassword = false;
        this.incorrectPassword = false;
    }

    changePassword(oldPassword: string, newPassword: string): void {
        this.accountDetailsService.popup = 'hidden';
        this.changePasswordService.changePassword(oldPassword, newPassword).subscribe(
            () => {
                this.closeComponent();
                this.accountDetailsService.popup = 'password';
                setTimeout(() => this.accountDetailsService.popup = 'hidden', 5000);
            },
            (err: HttpErrorResponse) => {
                if (err.status === 400) {
                    this.incorrectPassword = true;
                } else if (err.status === 409) {
                    this.samePassword = true;
                } else {
                    this.errorHandlerService.handleError(err);
                }
            }
        );
    }
}
