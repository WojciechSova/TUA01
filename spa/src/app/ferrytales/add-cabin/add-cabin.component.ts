import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FerryDetailsService } from '../../services/mop/ferry-details.service';

@Component({
    selector: 'app-add-cabin',
    templateUrl: './add-cabin.component.html',
    styleUrls: ['./add-cabin.component.less']
})
export class AddCabinComponent implements OnInit {

    name = '';

    constructor(private router: Router,
                private ferryDetailsService: FerryDetailsService) {
        this.name = ferryDetailsService.ferry.name;
    }

    form = new FormGroup({
        capacity: new FormControl('', [Validators.required, Validators.min(1)]),
        number: new FormControl('', [Validators.required, Validators.pattern('[A-Z][0-9]{3}')]),
    });

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerriesListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/ferries']);
    }

    goToFerryBreadcrumb(): void {
        this.router.navigate(['/ferrytales/ferries/' + this.name])
    }

    addCabin(capacity: string, cabinType: string, number: string): any {
        //TODO zamiana na właściwą metodę przy łączeniu spa z endpointem
        console.log("POJEMNOSC: " + capacity + "\nTYP: " + cabinType + "\nNUMER:" + number)
    }

    getCurrentType(): string {
        var selector = document.querySelector('input[name="radio"]:checked');
        if (selector != null) {
            return (selector as HTMLInputElement).value;
        } else {
            return "First class";
        }
    }

}
