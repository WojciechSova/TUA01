import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validatePassword } from '../../common/navigation/register/matching.validator';
import { ChangePasswordService } from '../../services/change-password.service';

@Component({
    selector: 'app-change-password-form',
    templateUrl: './change-password-form.component.html',
    styleUrls: ['./change-password-form.component.less']
})
export class ChangePasswordFormComponent {

    constructor(private changePasswordService: ChangePasswordService) {
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
    }

    changePassword(oldPassword: string, newPassword: string): void {
        this.changePasswordService.changePassword(oldPassword, newPassword).subscribe();
    }
}
