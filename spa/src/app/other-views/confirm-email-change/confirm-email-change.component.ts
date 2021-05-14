import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChangeEmailService } from '../../services/change-email.service';

@Component({
  selector: 'app-confirm-email-change',
  templateUrl: './confirm-email-change.component.html',
  styleUrls: ['./confirm-email-change.component.less']
})
export class ConfirmEmailChangeComponent implements OnInit {

    timeout = 5000;
    success = false;
    visible = false;
    url = '';

    constructor(private route: ActivatedRoute,
                private router: Router,
                private changeEmailService: ChangeEmailService) {
        this.url = this.route.snapshot.paramMap.get('url') as string;

        this.changeEmailService.confirmEmailChange(this.url).subscribe(
            () => {
                this.success = true;
                this.visible = true;
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            },
            () => {
                this.success = false;
                this.visible = true;
                setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            }
        );
    }

    ngOnInit(): void {
    }

}
