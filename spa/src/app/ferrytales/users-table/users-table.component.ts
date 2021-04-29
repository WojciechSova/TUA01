import { Component } from '@angular/core';
import { AccountGeneral } from '../../model/mok/AccountGeneral';
import { AccountGeneralService } from '../../services/accountGeneral.service';


@Component({
    selector: 'app-users-table',
    templateUrl: './users-table.component.html',
    styleUrls: ['./users-table.component.less']
})
export class UsersTableComponent {

    accountGeneralList: AccountGeneral[] = [];

    constructor(private accountGeneralService: AccountGeneralService) {
        this.getAccounts();
    }

    getAccounts(): void {
        this.accountGeneralService.getAccounts().subscribe(
            (response: AccountGeneral[]) => this.accountGeneralList = response);
    }
}
