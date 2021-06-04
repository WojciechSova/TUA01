import { Component, Input } from '@angular/core';
import { AuthService } from '../../../services/mok/auth.service';
import { IdentityService } from '../../../services/utils/identity.service';
import { Router } from '@angular/router';
import { SessionUtilsService } from '../../../services/utils/session-utils.service';

@Component({
    selector: 'app-links',
    templateUrl: './links.component.html',
    styleUrls: ['./links.component.less']
})
export class LinksComponent {

    constructor(private authService: AuthService,
                public identityService: IdentityService,
                private router: Router,
                private sessionUtilsService: SessionUtilsService) {
        sessionUtilsService.isSessionTimeoutVisibleChange.subscribe(value => {
            this.isSessionTimeoutVisible = value;
            this.sessionUtilsService.timeoutVisible = value;
        }
        );
        sessionUtilsService.isSessionNearlyTimeoutVisibleChange.subscribe(value => {
            this.isSessionNearlyTimeoutVisible = value;
            this.sessionUtilsService.nearlyTimeoutVisible = value;
        }
        );
    }

    @Input()
    public stickyStyles = true;

    isLoginVisible = false;
    isRegisterVisible = false;
    isResetPasswordVisible = false;
    isSessionTimeoutVisible = false;
    isSessionNearlyTimeoutVisible = false;

    signOut(): void {
        this.authService.signOut();
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

    changeSessionTimeoutVisible(visible: boolean): void {
        this.isSessionTimeoutVisible = visible;
        this.sessionUtilsService.timeoutVisible = visible;
    }

    changeSessionNearlyTimeoutVisible(visible: boolean): void {
        this.isSessionNearlyTimeoutVisible = visible;
    }

    getProfile(): void {
        this.router.navigate(['/ferrytales/accounts', this.identityService.getLogin()]);
    }

    getAccessLevels(): string[] {
        return this.identityService.getAllRolesAsString().split(',');
    }

    setCurrentAccessLevel(level: string): void {
        this.identityService.setCurrentRole(level);
    }
}
