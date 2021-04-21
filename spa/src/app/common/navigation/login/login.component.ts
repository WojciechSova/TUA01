import { Component, EventEmitter, Output } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.less']
})
export class LoginComponent {

    constructor(private authService: AuthService) {
    }

    @Output()
    isLoginVisibleChange = new EventEmitter<boolean>();

    @Output()
    isRegisterVisibleChange = new EventEmitter<boolean>();

    login = '';
    password = '';

    auth(): void {
        this.authService.auth(this.login, this.password).subscribe(
            (response: string) => {
                this.authService.setSession(response);
                this.closeComponent();
            }
        );
    }

    closeComponent(): void {
        this.isLoginVisibleChange.emit(false);
        this.isRegisterVisibleChange.emit(false);
    }

    openRegister(): void {
        this.isLoginVisibleChange.emit(false);
        this.isRegisterVisibleChange.emit(true);
    }
}
