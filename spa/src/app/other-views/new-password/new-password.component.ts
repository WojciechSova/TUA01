import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {ResetPasswordService} from '../../services/reset-password.service';

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.less']
})
export class NewPasswordComponent {

    timeout = 8000;
    success = true;
    visible = true;
    url = '';
    newPassword = '';

    constructor(private route: ActivatedRoute,
                private router: Router,
                private resetPasswordService: ResetPasswordService) {
        this.url = this.route.snapshot.paramMap.get('url') as string;
    }

    sendNewPassword(): void {
        this.resetPasswordService.setNewPassword(this.url, this.newPassword).subscribe(
            () => {
                this.success = true;
                this.visible = true;
                // setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            },
            () => {
                this.success = false;
                this.visible = true;
                // setTimeout(() => this.router.navigateByUrl('/'), this.timeout);
            }
        );
    }
}
