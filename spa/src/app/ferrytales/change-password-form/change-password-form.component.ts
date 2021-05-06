import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validatePassword } from '../../common/navigation/register/matching.validator';

@Component({
    selector: 'app-change-password-form',
    templateUrl: './change-password-form.component.html',
    styleUrls: ['./change-password-form.component.less']
})
export class ChangePasswordFormComponent implements OnInit {

    constructor() {
    }

    @Output()
    isChangePasswordFormVisibleChange = new EventEmitter<boolean>();

    form = new FormGroup({
        oldPassword: new FormControl('', [Validators.required, validatePassword]),
        newPassword: new FormControl('', [Validators.required, Validators.minLength(8), validatePassword]),
        newPasswordRepeat: new FormControl('', [Validators.required, validatePassword]),
    });

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.isChangePasswordFormVisibleChange.emit(false);
    }

}
