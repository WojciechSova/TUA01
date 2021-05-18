import { Component, Output, EventEmitter } from '@angular/core';
import { ResetPasswordService } from '../../../services/reset-password.service';
import { AccountDetailsService } from '../../../services/account-details.service';

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
    isConfirmPasswordVisible = new EventEmitter<boolean>();

    confirmPasswordReset(): void {
        this.resetPasswordService.resetPasswordResponse(this.accountDetailsService.account.email).subscribe(
            () => this.closeComponent()
        );
        this.closeComponent();
    }

    closeComponent(): void {
        this.isConfirmPasswordVisible.emit(false);
    }

}
