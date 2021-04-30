import { Component } from '@angular/core';
import { AccountGeneral } from '../../model/mok/AccountGeneral';
import { AccountGeneralService } from '../../services/account-general.service';
import { Router } from '@angular/router';


@Component({
    selector: 'app-users-table',
    templateUrl: './users-table.component.html',
    styleUrls: ['./users-table.component.less']
})
export class UsersTableComponent {

    accountGeneralList: AccountGeneral[] = [];

    constructor(private accountGeneralService: AccountGeneralService, private router: Router) {
        this.getAccounts();
    }

    getAccounts(): void {
        this.accountGeneralService.getAccounts().subscribe(
            (response: AccountGeneral[]) => this.accountGeneralList = response);
    }

    setUser(login: string): void {
        this.router.navigate(['/ferrytales/accounts', login]);
    }
}
