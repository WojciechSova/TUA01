import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'app-add-cruise',
    templateUrl: './add-cruise.component.html',
    styleUrls: ['./add-cruise.component.less']
})
export class AddCruiseComponent implements OnInit {

    code = '';

    constructor(private route: ActivatedRoute,
                private router: Router) {
        this.code = this.route.snapshot.paramMap.get('code') as string;
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToRoutesListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/routes']);
    }

    goToRouteDetailsBreadcrumb(): void {
        this.router.navigate(['/ferrytales/routes/' + this.code]);
    }
}
