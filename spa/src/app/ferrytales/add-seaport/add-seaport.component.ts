import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SeaportGeneralService } from '../../services/mop/seaport-general.service';
import { SeaportGeneral } from '../../model/mop/SeaportGeneral';

@Component({
    selector: 'app-add-seaport',
    templateUrl: './add-seaport.component.html',
    styleUrls: ['./add-seaport.component.less']
})
export class AddSeaportComponent implements OnInit {

    error = false;

    constructor(private router: Router,
                private seaportGeneralService: SeaportGeneralService) {
    }

    ngOnInit(): void {
    }

    form = new FormGroup({
        city: new FormControl('', [Validators.required, Validators.max(30)]),
        code: new FormControl('', [Validators.required, Validators.pattern('[A-Z]{3}')])
    });

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToSeaportListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/seaports']);
    }

    addSeaport(newCity: string, newCode: string): any {
        const seaport: SeaportGeneral = {
            city: newCity,
            code: newCode
        };

        this.seaportGeneralService.addSeaport(seaport).subscribe(
            () => this.goToSeaportListBreadcrumb(),
            (error: any) => this.error = true
        );
    }
}
