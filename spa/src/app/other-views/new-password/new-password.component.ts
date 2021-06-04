import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ResetPasswordService } from '../../services/mok/reset-password.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validatePassword } from '../../common/navigation/register/matching.validator';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.less']
})
export class NewPasswordComponent {

    timeout = 8000;
    samePassword = false;
    incorrectPassword = false;
    url = '';
    newPassword = '';

    invalidUrlVisible = false;
    tooShortVisible = false;
    changeSuccessful = false;
    otherError = false;

    form = new FormGroup({
        password: new FormControl('', [Validators.required, Validators.minLength(8), validatePassword]),
        passwordRepeat: new FormControl('', [Validators.required, validatePassword]),
    });

    constructor(private route: ActivatedRoute,
                private router: Router,
                private resetPasswordService: ResetPasswordService) {
        this.url = this.route.snapshot.paramMap.get('url') as string;
    }

    getChangeSuccessful(): boolean {
        return this.changeSuccessful;
    }

    sendNewPassword(password: string, passwordRepeat: string): void {
        if (password !== passwordRepeat) {
            this.samePassword = false;
            return;
        } else if (password.length < 8) {
            this.tooShortVisible = true;
            return;
        }
        this.invalidUrlVisible = false;
        this.tooShortVisible = false;
        this.changeSuccessful = false;
        this.otherError = false;
        this.resetPasswordService.setNewPassword(this.url, password).subscribe(
            () => {
                this.changeSuccessful = true;
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            },
            (error: HttpErrorResponse) => {
                if (error.status === 400) {
                    this.invalidUrlVisible = true;
                } else if (error.status === 406) {
                    this.tooShortVisible = true;
                } else {
                    this.otherError = true;
                }
            }
        );
    }
}
