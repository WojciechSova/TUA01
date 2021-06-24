import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChangeEmailService } from '../../services/mok/change-email.service';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';

@Component({
  selector: 'app-confirm-email-change',
  templateUrl: './confirm-email-change.component.html',
  styleUrls: ['./confirm-email-change.component.less']
})
export class ConfirmEmailChangeComponent implements OnInit {

    timeout = 5000;
    result = '';
    visible = false;
    url = '';

    constructor(private route: ActivatedRoute,
                private router: Router,
                private changeEmailService: ChangeEmailService,
                private errorHandlerService: ErrorHandlerService) {
        this.url = this.route.snapshot.paramMap.get('url') as string;

        this.changeEmailService.confirmEmailChange(this.url).subscribe(
            () => {
                this.result = 'success';
                this.visible = true;
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            },
            (error: any) => {
                if (error.status === 400) {
                    this.result = 'badRequest';
                    this.visible = true;
                } else if (error.status === 410) {
                    this.result = 'gone';
                    this.visible = true;
                } else {
                    this.errorHandlerService.handleError(error);
                }
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            }
        );
    }

    ngOnInit(): void {
    }

}
