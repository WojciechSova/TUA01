import { Component, Input } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { IdentityService } from '../../../services/utils/identity.service';

@Component({
    selector: 'app-links',
    templateUrl: './links.component.html',
    styleUrls: ['./links.component.less']
})
export class LinksComponent {

    constructor(private authService: AuthService, public identityService: IdentityService) {
    }

    @Input()
    public stickyStyles = true;

    isLoginVisible = false;
    isRegisterVisible = false;

    signOut(): void {
        this.authService.signOut();
    }

    changeLoginVisible(visible: boolean): void {
        this.isLoginVisible = visible;
    }

    changeRegisterVisible(visible: boolean): void {
        this.isRegisterVisible = visible;
    }
}
