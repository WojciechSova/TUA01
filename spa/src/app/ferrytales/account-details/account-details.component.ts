import {Component, OnInit} from '@angular/core';
import {AccountDetails} from '../../model/mok/AccountDetails';
import {IdentityService} from '../../services/utils/identity.service';
import {AccessLevel} from '../../model/mok/AccessLevel';

@Component({
    selector: 'app-account-details',
    templateUrl: './account-details.component.html',
    styleUrls: ['./account-details.component.less']
})
export class AccountDetailsComponent implements OnInit {

    constructor(public identityService: IdentityService) {
    }

    account: AccountDetails = {
        login: 'testlogin',
        password: 'raczejNiePokazujemy',
        active: true,
        confirmed: true,
        firstName: 'Daniel',
        lastName: 'Łondka',
        email: 'ssbd02@edu.p.lodz.pl',
        phoneNumber: '513954710',
        accessLevel: [
            {
                level: 'CLIENT',
                active: true,
                creationDate: new Date()
            },
            {
                level: 'ADMIN',
                active: false,
                creationDate: new Date()
            }
        ],
        timeZone: 'string',
        creationDate: new Date(),
        numberOfBadLogins: 0
    };

    ngOnInit(): void {
    }

    hasClientAccessLevel(accessLevel: AccessLevel): boolean {
        return accessLevel.level.includes('CLIENT');
    }
}
