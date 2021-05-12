import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-confirm-account',
  templateUrl: './confirm-account.component.html',
  styleUrls: ['./confirm-account.component.less']
})
export class ConfirmAccountComponent {

    success = false;
    url = '';

  constructor(private route: ActivatedRoute, private router: Router) {
      this.url = this.route.snapshot.paramMap.get('url') as string;

      // TODO Change to observable after add confirm-account service
      this.url === 'invalid' ? this.success = false : this.success = true;
      setTimeout(() => this.router.navigateByUrl('/'), 5000);
  }
}
