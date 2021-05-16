import { Component, OnInit } from '@angular/core';
import { AccountDetails } from '../../model/mok/AccountDetails';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AccountDetailsService } from '../../services/account-details.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UpdateAccountService } from '../../services/update-account.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-edit-user',
    templateUrl: './edit-user.component.html',
    styleUrls: ['./edit-user.component.less']
})
export class EditUserComponent implements OnInit {

    constructor(public accountDetailsService: AccountDetailsService,
                private route: ActivatedRoute,
                private updateAccountService: UpdateAccountService,
                private router: Router,
                private identityService: IdentityService) {
    }

    public existingPhoneNumber = false;
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
        phoneNumber: new FormControl('', [Validators.pattern('[0-9]{11}')]),
        timeZone: new FormControl('')
    });

    showUser(login: string): void {
        this.router.navigate(['/ferrytales/accounts', login]);
    }

    isUpdating(): boolean {
        return this.updating;
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

    editUser(firstName?: string, lastName?: string, phoneNumber?: string, timeZone?: string): void {
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

        this.sendEditRequest(acc).subscribe(
            () => {
                this.router.navigate(['ferrytales/accounts/' + acc.login]);
                this.updating = true;
                this.getAccount();
            },
            (err: HttpErrorResponse) => {
                if (err.status === 409) {
                    this.existingPhoneNumber = true;
                    this.getAccount();
                }
            }
        );
    }

    sendEditRequest(acc: AccountDetails): Observable<object> {
        if (localStorage.getItem('login') === acc.login) {
            return this.updateAccountService.updateOwnAccount(acc);
        } else {
            return this.updateAccountService.updateAccount(acc);
        }
    }

    ngOnInit(): void {
    }

}
