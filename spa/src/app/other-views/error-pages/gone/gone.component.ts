import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-gone',
    templateUrl: './gone.component.html',
    styleUrls: ['./gone.component.less']
})
export class GoneComponent implements OnInit {

    constructor(private router: Router) {
    }

    goToHomePage(): void {
        this.router.navigate(['/']);
    }

    ngOnInit(): void {
    }

}
