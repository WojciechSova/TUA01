import { Component } from '@angular/core';
import { AccountGeneral } from '../../model/mok/AccountGeneral';
import { AccountGeneralService } from '../../services/mok/account-general.service';
import { Router } from '@angular/router';
import { AccountDetailsService } from '../../services/mok/account-details.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';
import { AccessLevelService } from '../../services/mok/access-level.service';

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
    block = 'hide';
    unblock = 'hide';

    constructor(private accountGeneralService: AccountGeneralService,
                private accountDetailsService: AccountDetailsService,
                private router: Router,
                private errorHandlerService: ErrorHandlerService,
                public accessLevelService: AccessLevelService) {
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

    showUserDetails(login: string): void {
        this.accountDetailsService.getAccountDetails(login).subscribe(
            (response) => {
                this.accountDetailsService.readAccountAndEtagFromResponse(response);
                this.router.navigate(['/ferrytales/accounts', login]);
            }
        );
    }

    sortByLogin(): void {
        this.accountGeneralService.accountGeneralList?.sort((a, b) => {
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
        this.accountGeneralService.accountGeneralList?.sort((a, b) => {
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
        this.accountGeneralService.accountGeneralList?.sort((a, b) => {
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
        this.block = 'hide';
        this.unblock = 'hide';
        this.accessLevelService.popup = 'hidden';
        this.accountGeneralService.blockAccount(login).subscribe(() => {
            this.getAccounts();
            this.block = 'success';
            setTimeout(() => this.block = 'hide', 5000);
        },
             (error: HttpErrorResponse) => {
                this.errorHandlerService.handleError(error);
            });
    }

    unblockAccount(login: string): void {
        this.block = 'hide';
        this.unblock = 'hide';
        this.accessLevelService.popup = 'hidden';
        this.accountGeneralService.unblockAccount(login).subscribe(() => {
            this.getAccounts();
            this.unblock = 'success';
            setTimeout(() => this.unblock = 'hide', 5000);
        }, (error: HttpErrorResponse) => {
            this.errorHandlerService.handleError(error);
        });
    }

    goToHomeBreadcrumb(): void {
        this.accessLevelService.popup = 'hidden';
        this.router.navigate(['/']);
    }
}
