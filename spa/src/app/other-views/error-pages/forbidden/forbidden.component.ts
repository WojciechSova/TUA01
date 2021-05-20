import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-forbidden',
    templateUrl: './forbidden.component.html',
    styleUrls: ['./forbidden.component.less']
})
export class ForbiddenComponent implements OnInit {

    timeout = 0;
    url = '';

    constructor(private route: ActivatedRoute,
                private router: Router) {}

    goToHomePage(): void {
        this.router.navigate(['/']);
    }

    ngOnInit(): void {
    }

}
