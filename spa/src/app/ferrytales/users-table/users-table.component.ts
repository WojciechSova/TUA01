import { Component } from '@angular/core';
import { MokService } from "../../services/mok.service";
import { AccountGeneral } from "../../model/mok/AccountGeneral";


@Component({
    selector: 'app-users-table',
    templateUrl: './users-table.component.html',
    styleUrls: ['./users-table.component.less']
})
export class UsersTableComponent {

    constructor(private mokService: MokService) {
    }

    getAccounts(): AccountGeneral[] {
        return this.mokService.getAccounts()
    }
}
