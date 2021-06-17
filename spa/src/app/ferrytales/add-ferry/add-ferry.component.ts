import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-add-ferry',
    templateUrl: './add-ferry.component.html',
    styleUrls: ['./add-ferry.component.less']
})
export class AddFerryComponent implements OnInit {

    constructor(private router: Router) {
    }

    form = new FormGroup({
        name: new FormControl('', [Validators.required, Validators.maxLength(30)]),
        vehicleCapacity: new FormControl('', [Validators.required, Validators.pattern('[0-9]{1,3}')]),
        onDeckCapacity: new FormControl('', [Validators.required, Validators.pattern('[1-9][0-9]{0,3}')])
    });

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerryListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/ferries']);
    }
}
