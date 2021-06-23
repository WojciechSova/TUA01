import { Component, OnInit } from '@angular/core';
import { AccountDetails } from '../../model/mok/AccountDetails';
import { IdentityService } from '../../services/utils/identity.service';
import { AccessLevel } from '../../model/mok/AccessLevel';
import { AccountDetailsService } from '../../services/mok/account-details.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import {AccessLevelService} from '../../services/mok/access-level.service';
import { TranslationService } from '../../services/utils/translation.service';

@Component({
    selector: 'app-account-details',
    templateUrl: './account-details.component.html',
    styleUrls: ['./account-details.component.less']
})
export class AccountDetailsComponent implements OnInit {

    isChangePasswordFormVisible = false;

    isChangeEmailFormVisible = false;

    isAccessLevelFormVisible = false;

    resetPasswordConnect = {
        isResetPasswordVisible: false,
        resetPasswordResponse: 'hide'
    };

    loginToChangeAccessLevel = '';
    loginAccessLevels = [''];

    constructor(public identityService: IdentityService,
                public accountDetailsService: AccountDetailsService,
                private route: ActivatedRoute,
                private router: Router,
                public accessLevelService: AccessLevelService,
                public translationService: TranslationService) {
    }

    ngOnInit(): void {
        this.getAccount();
    }

    setLoginAccessLevels(login: string, accessLevels: AccessLevel[]): void {
        const accessLevelsStringTab = accessLevels.filter(accessLevel => accessLevel.active)
            .map((accessLevel) => accessLevel.level);
        this.loginToChangeAccessLevel = login;
        this.loginAccessLevels = accessLevelsStringTab;
    }

    hasClientAccessLevel(accessLevel: AccessLevel): boolean {
        return accessLevel.level === 'CLIENT';
    }

    getAccount(): void {
        this.resetPasswordConnect.resetPasswordResponse = 'hide';
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

    changeChangePasswordFormVisible(visible: boolean): void {
        this.accountDetailsService.popup = 'hidden';
        this.accessLevelService.popup = 'hidden';
        this.isChangePasswordFormVisible = visible;
    }

    resetPasswordClick(): void {
        this.accountDetailsService.popup = 'hidden';
        this.accessLevelService.popup = 'hidden';
        this.resetPasswordConnect.isResetPasswordVisible = true;
        this.resetPasswordConnect.resetPasswordResponse = 'hide';
        this.changePasswordResetVisible(this.resetPasswordConnect);
    }

    changeEmailFormVisible(visible: boolean): void {
        this.accountDetailsService.popup = 'hidden';
        this.accessLevelService.popup = 'hidden';
        this.isChangeEmailFormVisible = visible;
    }

    changeAccessLevelFormVisible(visible: boolean): void {
        this.accountDetailsService.popup = 'hidden';
        this.accessLevelService.popup = 'hidden';
        if (!visible) {
            this.getAccount();
        }

        this.isAccessLevelFormVisible = visible;
    }

    changePasswordResetVisible(resetResponse: any): void {
        this.accountDetailsService.popup = 'hidden';
        this.accessLevelService.popup = 'hidden';
        this.getAccount();
        this.resetPasswordConnect.isResetPasswordVisible = resetResponse.isResetPasswordVisible;
        this.resetPasswordConnect.resetPasswordResponse = resetResponse.resetPasswordResponse;
    }

    editUser(login: string): void {
        this.accountDetailsService.popup = 'hidden';
        this.accessLevelService.popup = 'hidden';
        this.router.navigate(['ferrytales/accounts/edit', login]);
    }

    isOnOwnProfile(): boolean {
        return this.identityService.getLogin() === this.accountDetailsService.account.login;
    }

    goToHomeBreadcrumb(): void {
        this.accountDetailsService.popup = 'hidden';
        this.accessLevelService.popup = 'hidden';
        this.router.navigate(['/']);
    }

    goToUserListBreadcrumb(): void {
        this.accountDetailsService.popup = 'hidden';
        this.accessLevelService.popup = 'hidden';
        this.router.navigate(['/ferrytales/accounts']);
    }
}
