import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { IdentityService } from '../../services/utils/identity.service';
import { AccessLevelService } from '../../services/mok/access-level.service';
import { forkJoin } from 'rxjs';

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
        this.accessLevelService.popup = 'hidden';
        forkJoin(
            {
                admin:  this.accessLevel.admin ?
                    this.accessLevelService.addAccessLevel(this.loginToChangeAccessLevel, 'ADMIN') :
                    this.accessLevelService.removeAccessLevel(this.loginToChangeAccessLevel, 'ADMIN'),
                employee: this.accessLevel.employee ?
                    this.accessLevelService.addAccessLevel(this.loginToChangeAccessLevel, 'EMPLOYEE') :
                    this.accessLevelService.removeAccessLevel(this.loginToChangeAccessLevel, 'EMPLOYEE'),
                client: this.accessLevel.client ?
                    this.accessLevelService.addAccessLevel(this.loginToChangeAccessLevel, 'CLIENT') :
                    this.accessLevelService.removeAccessLevel(this.loginToChangeAccessLevel, 'CLIENT'),
            }
        ).subscribe(() => {
            this.closeComponent();
            this.accessLevelService.popup = 'edit_success';
            setTimeout(() => this.accessLevelService.popup = 'hidden', 5000);
        });
    }

    closeComponent(): void {
        this.isAccessLevelFormVisibleChange.emit(false);
    }
}
