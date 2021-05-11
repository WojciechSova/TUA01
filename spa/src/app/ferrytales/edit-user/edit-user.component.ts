import {Component, OnInit} from '@angular/core';
import {AccountDetails} from '../../model/mok/AccountDetails';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AccountDetailsService} from '../../services/account-details.service';
import {ActivatedRoute, Router} from '@angular/router';
import {UpdateAccountService} from '../../services/update-account.service';


@Component({
    selector: 'app-edit-user',
    templateUrl: './edit-user.component.html',
    styleUrls: ['./edit-user.component.less']
})
export class EditUserComponent implements OnInit {

    constructor(public accountDetailsService: AccountDetailsService,
                private route: ActivatedRoute,
                private updateAccountService: UpdateAccountService,
                private router: Router) {
        this.getAccount();
    }

    public timezones: string[] = [
        'UTC-12',
        'UTC-11',
        'UTC-10',
        'UTC-9',
        'UTC-8',
        'UTC-7',
        'UTC-6',
        'UTC-5',
        'UTC-4',
        'UTC-3',
        'UTC-2',
        'UTC-1',
        'UTC+0',
        'UTC+1',
        'UTC+2',
        'UTC+3',
        'UTC+4',
        'UTC+5',
        'UTC+6',
        'UTC+7',
        'UTC+8',
        'UTC+9',
        'UTC+10',
        'UTC+11',
        'UTC+12'
    ];
    private updating = false;

    form = new FormGroup({
        firstName: new FormControl(''),
        lastName: new FormControl(''),
        phoneNumber: new FormControl('', [Validators.pattern('[0-9]{11}')]),
        language: new FormControl(''),
        timeZone: new FormControl('')
    });

    setUser(login: string): void {
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
        this.updateAccountService.getAccountETag(login).subscribe(resp => {
            this.updateAccountService.eTag = resp.headers.get('ETag') as string;
            this.accountDetailsService.account = resp.body as AccountDetails;
        });
    }

    editUser(firstName?: string, lastName?: string, phoneNumber?: string, language?: string, timeZone?: string): void {
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
            acc.language = language;
        }
        if (lastName != null) {
            acc.timeZone = timeZone;
        }

        this.updateAccountService.updateAccount(acc).subscribe(() => this.router.navigate(['ferrytales/accounts/' + acc.login]));
        this.updating = true;
    }

    ngOnInit(): void {
    }

}
