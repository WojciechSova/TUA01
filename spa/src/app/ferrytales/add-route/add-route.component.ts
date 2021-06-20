import { Component , OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl , FormGroup, Validators } from '@angular/forms';
import { SeaportGeneralService } from '../../services/mop/seaport-general.service';
import { SeaportGeneral } from '../../model/mop/SeaportGeneral';

@Component({
    selector: 'app-add-route',
    templateUrl: './add-route.component.html',
    styleUrls: ['./add-route.component.less']
})
export class AddRouteComponent implements OnInit {

    form = new FormGroup({
        code: new FormControl('', [Validators.required, Validators.pattern('[A-Z]{6}')]),
        start: new FormControl('', [Validators.required]),
        dest: new FormControl('', [Validators.required]),
    });

    start = '';
    dest = '';

    constructor(private router: Router,
                public seaportGeneralService: SeaportGeneralService) {
        seaportGeneralService.getSeaports().subscribe(
            (seaports: SeaportGeneral[]) => seaportGeneralService.seaportsList = seaports
        );
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToRouteListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/routes']);
    }

    addRoute(value: string): void {
        return;
    }

    getSeaports(): SeaportGeneral[] {
        return this.seaportGeneralService.seaportsList.filter(
            seaport => seaport.code !== this.start && seaport.code !== this.dest
        );
    }

    getCity(code: string): string | undefined {
        return this.seaportGeneralService.seaportsList.find(seaport => seaport.code === code)?.city;
    }
}
