import { Component, Input } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { IdentityService } from '../../../services/utils/identity.service';
import { AccountDetailsService } from '../../../services/account-details.service';
import { Router } from '@angular/router';
import { AccountGeneralService } from '../../../services/account-general.service';
import { UpdateAccountService } from '../../../services/update-account.service';

@Component({
    selector: 'app-links',
    templateUrl: './links.component.html',
    styleUrls: ['./links.component.less']
})
export class LinksComponent {

    constructor(private authService: AuthService,
                public identityService: IdentityService,
                private accountDetailsService: AccountDetailsService,
                private accountGeneralService: AccountGeneralService,
                private updateAccountService: UpdateAccountService,
                private router: Router) {
    }

    @Input()
    public stickyStyles = true;

    isLoginVisible = false;
    isRegisterVisible = false;
    isResetPasswordVisible = false;

    signOut(): void {
        this.authService.signOut();
        this.accountDetailsService.ngOnDestroy();
        this.accountGeneralService.ngOnDestroy();
        this.updateAccountService.ngOnDestroy();
    }

    changeLoginVisible(visible: boolean): void {
        this.isLoginVisible = visible;
    }

    changeRegisterVisible(visible: boolean): void {
        this.isRegisterVisible = visible;
    }

    changeResetPasswordVisible(visible: boolean): void {
        this.isResetPasswordVisible = visible;
    }

    getProfile(): void {
        this.accountDetailsService.getProfile().subscribe(
            (response) => {
                this.accountDetailsService.readAccountAndEtagFromResponse(response);
                this.router.navigate(['/ferrytales/accounts', response.body?.login]);
            }
        );
    }

    getAccessLevels(): string[] {
        return this.identityService.getAllRolesAsString().split(',');
    }

    setCurrentAccessLevel(level: string): void {
        this.identityService.setCurrentRole(level);
    }
}
