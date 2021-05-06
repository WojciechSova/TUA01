import { Component, Input } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { IdentityService } from '../../../services/utils/identity.service';
import { AccountDetailsService } from '../../../services/account-details.service';
import { Router } from '@angular/router';
import { AccountGeneralService } from "../../../services/account-general.service";

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
                private router: Router) {
    }

    @Input()
    public stickyStyles = true;

    isLoginVisible = false;
    isRegisterVisible = false;

    signOut(): void {
        this.authService.signOut();
        this.accountDetailsService.ngOnDestroy();
        this.accountGeneralService.ngOnDestroy();
    }

    changeLoginVisible(visible: boolean): void {
        this.isLoginVisible = visible;
    }

    changeRegisterVisible(visible: boolean): void {
        this.isRegisterVisible = visible;
    }

    getProfile(): void {
        this.accountDetailsService.getProfile().subscribe(
            (acc) => {
                this.accountDetailsService.account = acc;
                this.router.navigateByUrl('/ferrytales/account');
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
