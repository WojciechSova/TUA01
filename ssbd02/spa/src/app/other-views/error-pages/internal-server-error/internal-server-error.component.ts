import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-internal-server-error',
    templateUrl: './internal-server-error.component.html',
    styleUrls: ['./internal-server-error.component.less']
})
export class InternalServerErrorComponent implements OnInit {

    constructor(private router: Router) {
    }

    goToHomePage(): void {
        this.router.navigate(['/']);
    }

    ngOnInit(): void {
    }
}
