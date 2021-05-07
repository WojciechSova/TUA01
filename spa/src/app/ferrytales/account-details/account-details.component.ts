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

    constructor(public identityService: IdentityService,
                public accountDetailsService: AccountDetailsService,
                private route: ActivatedRoute) {
        this.getAccount();
    }

    isChangePasswordFormVisible = false;

    isAccessLevelFormVisible = false;

    ngOnInit(): void {
    }

    hasClientAccessLevel(accessLevel: AccessLevel): boolean {
        return accessLevel.level === 'CLIENT';
    }

    getAccount(): void {
        const login = (this.route.snapshot.paramMap.get('login') as string);
        if (!login) {
            return;
        }
        this.accountDetailsService.getAccountDetails(login).subscribe(
            (accountDetails: AccountDetails) => this.accountDetailsService.account = accountDetails);
    }

    changeChangePasswordFormVisible(visible: boolean): void {
        this.isChangePasswordFormVisible = visible;
    }

    changeAccessLevelFormVisible(visible: boolean): void {
        this.isAccessLevelFormVisible = visible;
    }
}
