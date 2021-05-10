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

    constructor(private accountGeneralService: AccountGeneralService, private router: Router) {
        this.getAccounts();
    }

    isAccessLevelFormVisible = false;

    loginToChangeAccessLevel = '';
    loginAccessLevels = [''];

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

    setUser(login: string): void {
        this.router.navigate(['/ferrytales/accounts', login]);
    }

    changeAccessLevelFormVisible(visible: boolean): void {
        this.isAccessLevelFormVisible = visible;
        this.getAccounts();
    }

    editUser(login: string): void {
        this.router.navigate(['ferrytales/accounts/edit', login]);
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
