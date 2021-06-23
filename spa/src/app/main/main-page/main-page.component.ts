import { Component } from '@angular/core';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-main-page',
    templateUrl: './main-page.component.html',
    styleUrls: ['./main-page.component.less']
})
export class MainPageComponent {

    constructor(public identityService: IdentityService) {
    }

    loginVisible = false;

    change(): void {
        this.loginVisible = false;
        setTimeout(() => {this.loginVisible = true; }, 10);
    }
}
