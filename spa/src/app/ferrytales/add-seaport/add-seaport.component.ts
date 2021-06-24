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

    form = new FormGroup({
        city: new FormControl('', [Validators.required, Validators.max(30)]),
        code: new FormControl('', [Validators.required, Validators.pattern('[A-Z]{3}')])
    });

    constructor(private router: Router,
                private seaportGeneralService: SeaportGeneralService) {
    }

    ngOnInit(): void {
    }

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
        this.seaportGeneralService.popup = 'hidden';
        this.seaportGeneralService.addSeaport(seaport).subscribe(
            () => {
                this.goToSeaportListBreadcrumb();
                this.seaportGeneralService.popup = 'add_seaport_success';
                setTimeout(() => this.seaportGeneralService.popup = 'hidden', 5000);
            },
            (error: any) => this.error = true
        );
    }
}
