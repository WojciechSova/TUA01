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

    searchedValue = '';
    isAccessLevelFormVisible = false;

    loginToChangeAccessLevel = '';
    loginAccessLevels = [''];

    byLogin = true;
    byFirstName = false;
    byLastName = false;

    constructor(private accountGeneralService: AccountGeneralService, private router: Router) {
        this.getAccounts();
    }

    setLoginAccessLevels(login: string, accessLevels: string[]): void {
        this.loginToChangeAccessLevel = login;
        this.loginAccessLevels = accessLevels;
    }

    getAccounts(): void {
        this.accountGeneralService.getAccounts().subscribe(
            (response: AccountGeneral[]) => {
                this.accountGeneralService.accountGeneralList = response;
                this.listAccounts();
            });
    }

    listAccounts(): AccountGeneral[] {
        this.byLogin && this.sortByLogin();
        this.byFirstName && this.sortByFirstName();
        this.byLastName && this.sortByLastName();
        return this.accountGeneralService.accountGeneralList;
    }

    sortByLogin(): void {
        this.accountGeneralService.accountGeneralList.sort((a, b) => {
            if (a.login.toLowerCase() < b.login.toLowerCase()) {
                return -1;
            }
            if (a.login.toLowerCase() > b.login.toLowerCase()) {
                return 1;
            }
            return 0;
        });
    }

    sortByFirstName(): void {
        this.accountGeneralService.accountGeneralList.sort((a, b) => {
            if (a.firstName.toLowerCase() < b.firstName.toLowerCase()) {
                return -1;
            }
            if (a.firstName.toLowerCase() > b.firstName.toLowerCase()) {
                return 1;
            }
            return 0;
        });
    }

    sortByLastName(): void {
        this.accountGeneralService.accountGeneralList.sort((a, b) => {
            if (a.lastName.toLowerCase() < b.lastName.toLowerCase()) {
                return -1;
            }
            if (a.lastName.toLowerCase() > b.lastName.toLowerCase()) {
                return 1;
            }
            return 0;
        });
    }

    setUser(login: string): void {
        this.router.navigate(['/ferrytales/accounts', login]);
    }

    changeAccessLevelFormVisible(visible: boolean): void {
        this.isAccessLevelFormVisible = visible;
        this.getAccounts();
    }

    blockAccount(login: string): void {
        this.accountGeneralService.blockAccount(login).subscribe(() => {
            this.getAccounts();
        });
    }

    unblockAccount(login: string): void {
        this.accountGeneralService.unblockAccount(login).subscribe(() => {
            this.getAccounts();
        });
    }

}
