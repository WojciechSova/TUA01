import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {IdentityService} from '../../services/utils/identity.service';

@Component({
    selector: 'app-ferrytales',
    templateUrl: './ferrytales.component.html',
    styleUrls: ['./ferrytales.component.less']
})
export class FerrytalesComponent implements OnInit {

    constructor(private router: Router,
                private identityService: IdentityService) {
    }


    ngOnInit(): void {
    }


    goToHomeBreadcrumb(): void {
            this.router.navigate(['/']);
    }

    goToUserListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/accounts']);
    }

    isAdminAndIsInAccountView(): boolean{
        return this.identityService.isAdmin() && window.location.href.includes('/#/ferrytales/accounts')
            && window.location.href.length > 'https://localhost:8181/#/ferrytales/accounts/'.length;
    }

}
