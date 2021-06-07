import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-add-seaport',
    templateUrl: './add-seaport.component.html',
    styleUrls: ['./add-seaport.component.less']
})
export class AddSeaportComponent implements OnInit {

    constructor(private router: Router) {
    }

    ngOnInit(): void {
    }

    form = new FormGroup({
        city: new FormControl('', [Validators.required, Validators.max(30)]),
        code: new FormControl('', [Validators.required, Validators.pattern('[A-Z]{3}')])
    })

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToSeaportListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/seaports']);
    }

    addSeaport(newCity: string, newCode: string): any {

    }
}
