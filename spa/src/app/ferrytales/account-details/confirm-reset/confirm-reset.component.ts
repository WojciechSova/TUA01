import { Component, Output, EventEmitter } from '@angular/core';
import { ResetPasswordService } from '../../../services/mok/reset-password.service';
import { AccountDetailsService } from '../../../services/mok/account-details.service';

@Component({
    selector: 'app-confirm-reset',
    templateUrl: './confirm-reset.component.html',
    styleUrls: ['./confirm-reset.component.less']
})
export class ConfirmResetComponent {

    constructor(private resetPasswordService: ResetPasswordService,
                private accountDetailsService: AccountDetailsService) {
    }

    @Output()
    isConfirmPasswordVisible = new EventEmitter<any>();

    resetPasswordConnect = {
        isResetPasswordVisible: true,
        resetPasswordResponse: 'hide'
    };

    confirmPasswordReset(): void {
        this.resetPasswordService.resetPasswordResponse(this.accountDetailsService.account.email).subscribe(
            () => {
                this.resetPasswordConnect.isResetPasswordVisible = false;
                this.resetPasswordConnect.resetPasswordResponse = 'success';
                this.isConfirmPasswordVisible.emit(this.resetPasswordConnect);
            },
            () => {
                this.resetPasswordConnect.isResetPasswordVisible = false;
                this.resetPasswordConnect.resetPasswordResponse = 'failure';
                this.isConfirmPasswordVisible.emit(this.resetPasswordConnect);
            }
        );
    }

    closeComponent(): void {
        this.resetPasswordConnect.isResetPasswordVisible = false;
        this.resetPasswordConnect.resetPasswordResponse = 'hide';
        this.isConfirmPasswordVisible.emit(this.resetPasswordConnect);
    }

}
