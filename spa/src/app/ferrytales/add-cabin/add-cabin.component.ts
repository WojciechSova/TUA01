import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FerryDetailsService } from '../../services/mop/ferry-details.service';
import { CabinGeneralService } from '../../services/mop/cabin-general.service';
import { CabinGeneral } from '../../model/mop/CabinGeneral';

@Component({
    selector: 'app-add-cabin',
    templateUrl: './add-cabin.component.html',
    styleUrls: ['./add-cabin.component.less']
})
export class AddCabinComponent implements OnInit {

    name = "";
    error: boolean = false;

    constructor(private router: Router,
                private ferryDetailsService: FerryDetailsService,
                private cabinGeneralService: CabinGeneralService) {
        this.name = ferryDetailsService.ferry.name;
    }

    ngOnInit(): void {
    }

    form = new FormGroup({
        capacity: new FormControl('', [Validators.required, Validators.min(1), Validators.max(99)]),
        number: new FormControl('', [Validators.required, Validators.pattern("[A-Z][0-9]{3}")]),
    })

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
        switch (cabinType) {
            case "Pierwsza klasa":
                cabinType = "First class";
                break;
            case "Druga klasa":
                cabinType = "Second class";
                break;
            case "Trzecia klasa":
                cabinType = "Third class";
                break;
            case "Klasa inwalidzka":
                cabinType = "Disabled class";
                break;
        }
        var cabin: CabinGeneral = {
            capacity: capacity,
            cabinType: cabinType,
            number: number
        }
        this.cabinGeneralService.addCabin(cabin, this.name).subscribe(
            () => this.goToFerryBreadcrumb(),
            (error: any) => this.error = true
        )
    }

    getCurrentType(): string {
        const selector = document.querySelector('input[name="radio"]:checked');
        if (selector != null) {
            return (selector as HTMLInputElement).value;
        } else {
            return "First class";
        }
    }

}
