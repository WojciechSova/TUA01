import { Component, OnInit } from '@angular/core';
import { AccountDetails } from '../../model/mok/AccountDetails';
import { IdentityService } from '../../services/utils/identity.service';
import { AccessLevel } from '../../model/mok/AccessLevel';
import { AccountDetailsService } from '../../services/account-details.service';
import { ActivatedRoute } from '@angular/router';


@Component({
    selector: 'app-account-details',
    templateUrl: './account-details.component.html',
    styleUrls: ['./account-details.component.less']
})
export class AccountDetailsComponent implements OnInit {

    account: AccountDetails = {
        accessLevel: [],
        active: false,
        confirmed: false,
        creationDate: new Date(),
        email: '',
        firstName: '',
        lastName: '',
        numberOfBadLogins: 0,
        login: '',
        password: ''
    };

    constructor(public identityService: IdentityService,
                private accountDetailsService: AccountDetailsService,
                private route: ActivatedRoute) {
        this.getAccount();
    }

    ngOnInit(): void {
    }

    hasClientAccessLevel(accessLevel: AccessLevel): boolean {
        return accessLevel.level === 'CLIENT';
    }

    getAccount(): void {
        const login = (this.route.snapshot.paramMap.get('login') as string);
        this.accountDetailsService.getAccountDetails(login).subscribe(
            (accountDetails: AccountDetails) => this.account = accountDetails);
    }
}
