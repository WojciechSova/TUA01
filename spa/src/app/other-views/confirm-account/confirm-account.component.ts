import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmService } from '../../services/confirm.service';

@Component({
    selector: 'app-confirm-account',
    templateUrl: './confirm-account.component.html',
    styleUrls: ['./confirm-account.component.less']
})
export class ConfirmAccountComponent {

    timeout = 5000;
    success = false;
    visible = false;
    url = '';

    constructor(private route: ActivatedRoute,
                private router: Router,
                private confirmService: ConfirmService) {
        this.url = this.route.snapshot.paramMap.get('url') as string;

        this.confirmService.confirmAccount(this.url).subscribe(
            () => {
                this.visible = true;
                this.success = true;
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            },
            () => {
                this.visible = true;
                this.success = false;
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            }
        );
    }
}
