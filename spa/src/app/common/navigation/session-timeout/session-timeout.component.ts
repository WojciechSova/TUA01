import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
    selector: 'app-session-timeout',
    templateUrl: './session-timeout.component.html',
    styleUrls: ['./session-timeout.component.less']
})
export class SessionTimeoutComponent implements OnInit {

    @Output()
    isSessionTimeoutVisibleChange = new EventEmitter<boolean>();

    constructor(private authService: AuthService) {
    }

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.authService.signOut();
        this.isSessionTimeoutVisibleChange.emit(false);
    }
}
