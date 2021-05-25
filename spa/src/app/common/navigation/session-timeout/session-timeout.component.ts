import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { SessionUtilsService } from "../../../services/utils/session-utils.service";

@Component({
    selector: 'app-session-timeout',
    templateUrl: './session-timeout.component.html',
    styleUrls: ['./session-timeout.component.less']
})
export class SessionTimeoutComponent implements OnInit {

    @Output()
    isSessionTimeoutVisibleChange = new EventEmitter<boolean>();

    @Output()
    isSessionNearlyTimeoutVisibleChange = new EventEmitter<boolean>();

    constructor(private authService: AuthService,
                public sessionUtilsService: SessionUtilsService) {
    }

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.authService.signOut();
        this.isSessionTimeoutVisibleChange.emit(false);
        this.isSessionNearlyTimeoutVisibleChange.emit(false);
    }

    refreshToken(): void {
        this.authService.refreshToken();
        this.isSessionNearlyTimeoutVisibleChange.emit(false);
    }
}
