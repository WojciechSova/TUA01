import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-forbidden',
    templateUrl: './forbidden.component.html',
    styleUrls: ['./forbidden.component.less']
})
export class ForbiddenComponent implements OnInit {

    constructor(private router: Router) {}

    goToHomePage(): void {
        this.router.navigate(['/']);
    }

    ngOnInit(): void {
    }

}
