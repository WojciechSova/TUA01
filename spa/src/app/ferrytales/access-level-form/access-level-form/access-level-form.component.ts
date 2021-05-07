import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { IdentityService } from '../../../services/utils/identity.service';
import { AccessLevelService } from '../../../services/access-level.service';

@Component({
    selector: 'app-access-level-form',
    templateUrl: './access-level-form.component.html',
    styleUrls: ['./access-level-form.component.less']
})
export class AccessLevelFormComponent implements OnChanges {

    @Input()
    loginToChangeAccessLevel = '';

    @Input()
    loginAccessLevels = [''];

    @Output()
    isAccessLevelFormVisibleChange = new EventEmitter<boolean>();

    accessLevel = {
        admin: false,
        employee: false,
        client: false
    };

    constructor(public identityService: IdentityService,
                public accessLevelService: AccessLevelService) {
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.accessLevel = {
            admin: false,
            employee: false,
            client: false
        };

        this.loginAccessLevels.forEach(accessLevel => {
            if (accessLevel === 'ADMIN') {
                this.accessLevel.admin = true;
            } else if (accessLevel === 'EMPLOYEE') {
                this.accessLevel.employee = true;
            } else if (accessLevel === 'CLIENT') {
                this.accessLevel.client = true;
            }
        });
    }

    confirm(): void {
        this.accessLevel.admin ?
            this.accessLevelService.addAccessLevel(this.loginToChangeAccessLevel, 'ADMIN').subscribe() :
            this.accessLevelService.removeAccessLevel(this.loginToChangeAccessLevel, 'ADMIN').subscribe();

        this.accessLevel.employee ?
            this.accessLevelService.addAccessLevel(this.loginToChangeAccessLevel, 'EMPLOYEE').subscribe() :
            this.accessLevelService.removeAccessLevel(this.loginToChangeAccessLevel, 'EMPLOYEE').subscribe();

        this.accessLevel.client ?
            this.accessLevelService.addAccessLevel(this.loginToChangeAccessLevel, 'CLIENT').subscribe() :
            this.accessLevelService.removeAccessLevel(this.loginToChangeAccessLevel, 'CLIENT').subscribe();

        this.closeComponent();
    }

    closeComponent(): void {
        this.isAccessLevelFormVisibleChange.emit(false);
    }
}
