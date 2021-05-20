import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validateEmail } from '../../common/navigation/register/matching.validator';
import { ChangeEmailService } from '../../services/change-email.service';
import { HttpErrorResponse } from '@angular/common/http';
import { AccountDetailsService } from '../../services/account-details.service';


@Component({
    selector: 'app-change-email-form',
    templateUrl: './change-email-form.component.html',
    styleUrls: ['./change-email-form.component.less']
})
export class ChangeEmailFormComponent implements OnInit {

    @Output()
    isChangeEmailFormVisible = new EventEmitter<boolean>();

    existingEmail = false;

    form = new FormGroup({
        email: new FormControl('', [Validators.required, Validators.email, validateEmail]),
        emailRepeat: new FormControl('', [Validators.required, validateEmail]),
    });

    constructor(private changeEmailService: ChangeEmailService,
                private accountDetailsService: AccountDetailsService) {
    }

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.isChangeEmailFormVisible.emit(false);
    }

    changeEmail(newEmail: string): void {
        if (this.accountDetailsService.account.login === localStorage.getItem('login')) {
            this.changeEmailService.changeEmail(newEmail).subscribe(
                () => {
                    this.closeComponent();
                },
                (err: HttpErrorResponse) => {
                    if (err.status === 409) {
                        this.existingEmail = true;
                    }
                }
            );
        } else {
            this.changeEmailService.changeOtherAccountEmail(this.accountDetailsService.account.login, newEmail).subscribe(
                () => {
                    this.closeComponent();
                },
                (err: HttpErrorResponse) => {
                    if (err.status === 409) {
                        this.existingEmail = true;
                    }
                }
            );
        }
    }
}
