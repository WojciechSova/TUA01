import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {SeaportGeneralService} from "../../services/mop/seaport-general.service";
import {SeaportDetails} from "../../model/mop/SeaportDetails";
import {SeaportGeneral} from "../../model/mop/SeaportGeneral";

@Component({
    selector: 'app-add-route',
    templateUrl: './add-route.component.html',
    styleUrls: ['./add-route.component.less']
})
export class AddRouteComponent implements OnInit {

    form = new FormGroup({
        code: new FormControl('', [Validators.required, Validators.pattern('[A-Z]{6}')]),
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
        console.log(this.start);
        console.log(this.seaportGeneralService.seaportsList);
    }

    setStartSeaport(seaport: string): void {
        this.start = seaport;
        console.log(this.start);
    }

    setDestSeaport(seaport: string): void {
        this.dest = seaport;
    }
}
