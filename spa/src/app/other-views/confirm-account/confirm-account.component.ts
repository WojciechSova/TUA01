import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmService } from '../../services/mok/confirm.service';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';

@Component({
    selector: 'app-confirm-account',
    templateUrl: './confirm-account.component.html',
    styleUrls: ['./confirm-account.component.less']
})
export class ConfirmAccountComponent {

    timeout = 5000;
    result = '';
    visible = false;
    url = '';

    constructor(private route: ActivatedRoute,
                private router: Router,
                private confirmService: ConfirmService,
                private errorHandlerService: ErrorHandlerService) {
        this.url = this.route.snapshot.paramMap.get('url') as string;

        this.confirmService.confirmAccount(this.url).subscribe(
            () => {
                this.visible = true;
                this.result = 'success';
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            },
            (error: any) => {
                if (error.status === 400) {
                    this.visible = true;
                    this.result = 'badUrl';
                } else if (error.status === 410) {
                    this.visible = true;
                    this.result = 'gone';
                } else {
                    this.visible = true;
                    this.errorHandlerService.handleError(error);
                }
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            }
        );
    }
}
