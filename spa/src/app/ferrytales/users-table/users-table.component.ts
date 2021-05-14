import { Component } from '@angular/core';
import { AccountGeneral } from '../../model/mok/AccountGeneral';
import { AccountGeneralService } from '../../services/account-general.service';
import { Router } from '@angular/router';
import { AccountDetailsService } from '../../services/account-details.service';
import {AccountDetails} from '../../model/mok/AccountDetails';

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

    constructor(private accountGeneralService: AccountGeneralService,
                private accountDetailsService: AccountDetailsService,
                private router: Router) {
        this.getAccounts();
    }

    setLoginAccessLevels(login: string, accessLevels: string[]): void {
        this.loginToChangeAccessLevel = login;
        this.loginAccessLevels = accessLevels;
    }

    getAccounts(): void {
        this.accountGeneralService.getAccounts().subscribe(
            (response: AccountGeneral[]) => this.accountGeneralService.accountGeneralList = response);
    }

    listAccounts(): AccountGeneral[] {
        return this.accountGeneralService.accountGeneralList;
    }

    showUserDetails(login: string): void {
        this.accountDetailsService.getAccountDetails(login).subscribe(
            (response) => {
                this.accountDetailsService.readAccountAndEtagFromResponse(response);
                this.router.navigate(['/ferrytales/accounts', login]);
            }
        );
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
