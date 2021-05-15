import { Component } from '@angular/core';
import { AccountDetails } from '../../model/mok/AccountDetails';
import { IdentityService } from '../../services/utils/identity.service';
import { AccessLevel } from '../../model/mok/AccessLevel';
import { AccountDetailsService } from '../../services/account-details.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'app-account-details',
    templateUrl: './account-details.component.html',
    styleUrls: ['./account-details.component.less']
})
export class AccountDetailsComponent {

    constructor(public identityService: IdentityService,
                public accountDetailsService: AccountDetailsService,
                private route: ActivatedRoute,
                private router: Router) {
    }

    isChangePasswordFormVisible = false;

    isChangeEmailFormVisible = false;

    isAccessLevelFormVisible = false;

    loginToChangeAccessLevel = '';
    loginAccessLevels = [''];

    setLoginAccessLevels(login: string, accessLevels: AccessLevel[]): void {
        const accessLevelsStringTab = accessLevels.map((accessLevel) => accessLevel.level);
        this.loginToChangeAccessLevel = login;
        this.loginAccessLevels = accessLevelsStringTab;
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
            (response: HttpResponse<AccountDetails>) => {
                this.accountDetailsService.readAccountAndEtagFromResponse(response);
            });
    }

    changeChangePasswordFormVisible(visible: boolean): void {
        this.isChangePasswordFormVisible = visible;
    }

    changeEmailFormVisible(visible: boolean): void {
        this.isChangeEmailFormVisible = visible;
    }

    changeAccessLevelFormVisible(visible: boolean): void {
        this.isAccessLevelFormVisible = visible;
    }

    editUser(login: string): void {
        this.router.navigate(['ferrytales/accounts/edit', login]);
    }
}
