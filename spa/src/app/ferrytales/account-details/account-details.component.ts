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

    isResetPasswordVisible = false;

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
        if (this.identityService.getLogin() === login) {
            this.accountDetailsService.getProfile().subscribe(
                (response: HttpResponse<AccountDetails>) => {
                    this.accountDetailsService.readAccountAndEtagFromResponse(response);
                });
        }
        else {
            this.accountDetailsService.getAccountDetails(login).subscribe(
                (response: HttpResponse<AccountDetails>) => {
                    this.accountDetailsService.readAccountAndEtagFromResponse(response);
                });
        }
    }

    changePasswordFormVisible(visible: boolean): void {
        this.isChangePasswordFormVisible = visible;
    }

    resetPasswordClick(): void {
        this.changePasswordResetVisible(true);
    }

    changeEmailFormVisible(visible: boolean): void {
        this.isChangeEmailFormVisible = visible;
    }

    changeAccessLevelFormVisible(visible: boolean): void {
        this.isAccessLevelFormVisible = visible;
    }

    changePasswordResetVisible(visible: boolean): void {
        this.getAccount();
        this.isResetPasswordVisible = visible;
    }

    editUser(login: string): void {
        this.router.navigate(['ferrytales/accounts/edit', login]);
    }

    isOnOwnProfile(): boolean {
        return this.identityService.getLogin() === this.accountDetailsService.account.login;
    }
}
