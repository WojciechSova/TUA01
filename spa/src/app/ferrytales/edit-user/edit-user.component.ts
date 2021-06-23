import { Component } from '@angular/core';
import { AccountDetails } from '../../model/mok/AccountDetails';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AccountDetailsService } from '../../services/mok/account-details.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UpdateAccountService } from '../../services/mok/update-account.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IdentityService } from '../../services/utils/identity.service';
import {ChangePasswordService} from "../../services/mok/change-password.service";
import {ErrorHandlerService} from "../../services/error-handlers/error-handler.service";

@Component({
    selector: 'app-edit-user',
    templateUrl: './edit-user.component.html',
    styleUrls: ['./edit-user.component.less']
})
export class EditUserComponent {

    isConfirmationVisible = false;
    firstName: string | undefined;
    lastName: string | undefined;
    phoneNumber: string | undefined;
    timeZone: string | undefined;

    constructor(public accountDetailsService: AccountDetailsService,
                private route: ActivatedRoute,
                private updateAccountService: UpdateAccountService,
                private router: Router,
                public identityService: IdentityService,
                private errorHandlerService: ErrorHandlerService) {
    }

    public existingPhoneNumber = false;
    public optimisticLockError = false;
    public unknownError = false;
    public timezones: string[] = [
        '-12:00',
        '-11:00',
        '-10:00',
        '-09:00',
        '-08:00',
        '-07:00',
        '-06:00',
        '-05:00',
        '-04:00',
        '-03:00',
        '-02:00',
        '-01:00',
        '+00:00',
        '+01:00',
        '+02:00',
        '+03:00',
        '+04:00',
        '+05:00',
        '+06:00',
        '+07:00',
        '+08:00',
        '+09:00',
        '+10:00',
        '+11:00',
        '+12:00',
        '+13:00',
        '+14:00'
    ];
    private updating = false;

    form = new FormGroup({
        firstName: new FormControl(''),
        lastName: new FormControl(''),
        phoneNumber: new FormControl('', [Validators.pattern('[0-9]{3,15}')]),
        timeZone: new FormControl('')
    });

    showUser(login: string): void {
        this.router.navigate(['/ferrytales/accounts', login]);
    }

    isUpdating(): boolean {
        return this.updating;
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToUserListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/accounts']);
    }

    goToAccountBreadcrumb(): void {
        this.router.navigate(['/ferrytales/accounts/' + this.accountDetailsService.account.login]);
    }

    getAccount(): void {
        const login = (this.route.snapshot.paramMap.get('login') as string);
        if (!login) {
            return;
        }
        if ((this.identityService.getLogin() === this.accountDetailsService.account.login)) {
            this.accountDetailsService.getProfile().subscribe(response =>
                this.accountDetailsService.readAccountAndEtagFromResponse(response)
            );
        } else {
            this.accountDetailsService.getAccountDetails(login).subscribe(response =>
                this.accountDetailsService.readAccountAndEtagFromResponse(response)
            );
        }
    }

    editUserClick(firstName?: string, lastName?: string, phoneNumber?: string, timeZone?: string): void {
        this.isConfirmationVisible = true;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.timeZone = timeZone;
    }

    editUser(firstName?: string, lastName?: string, phoneNumber?: string, timeZone?: string): void {
        this.clearErrors();
        const acc: AccountDetails = this.accountDetailsService.account;
        if (firstName != null) {
            acc.firstName = firstName;
        }
        if (lastName != null) {
            acc.lastName = lastName;
        }
        if (lastName != null) {
            acc.phoneNumber = phoneNumber;
        }
        if (lastName != null) {
            acc.timeZone = timeZone;
        }

        this.accountDetailsService.popup = 'hidden';
        this.sendEditRequest(acc).subscribe(
            () => {
                this.router.navigate(['ferrytales/accounts/' + acc.login]);
                this.updating = true;
                this.accountDetailsService.popup = 'edit_user_success';
                setTimeout(() => this.accountDetailsService.popup = 'hidden', 5000);
                this.getAccount();
            },
            (err: HttpErrorResponse) => {
                if (err.error === 'ERROR.PHONE_NUMBER_UNIQUE') {
                    this.existingPhoneNumber = true;
                } else if (err.error === 'ERROR.OPTIMISTIC_LOCK') {
                    this.optimisticLockError = true;
                } else {
                    this.errorHandlerService.handleError(err);
                }
                this.getAccount();
            }
        );
    }

    sendEditRequest(acc: AccountDetails): Observable<object> {
        if (this.identityService.getLogin() === acc.login) {
            return this.updateAccountService.updateOwnAccount(acc);
        } else {
            return this.updateAccountService.updateAccount(acc);
        }
    }

    private clearErrors(): void {
        this.optimisticLockError = false;
        this.existingPhoneNumber = false;
        this.unknownError = false;
        this.accountDetailsService.popup = 'hidden';
    }

    confirmationResult(confirmationResult: boolean): void {
        this.isConfirmationVisible = false;
        if (confirmationResult) {
            this.editUser(this.firstName, this.lastName, this.phoneNumber, this.timeZone);
        }
    }

}
