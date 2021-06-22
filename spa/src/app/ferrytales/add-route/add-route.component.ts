import { Component , OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl , FormGroup, Validators } from '@angular/forms';
import { SeaportGeneralService } from '../../services/mop/seaport-general.service';
import { SeaportGeneral } from '../../model/mop/SeaportGeneral';
import { RouteGeneralService } from '../../services/mop/route-general.service';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';

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
    routeCode = '';
    startDestinationUniqueViolation = false;
    codeUniqueViolation = false;

    constructor(private router: Router,
                public seaportGeneralService: SeaportGeneralService,
                private routeGeneralService: RouteGeneralService,
                private errorHandlerService: ErrorHandlerService) {
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

    addRoute(): void {
        this.clearErrors();
        this.routeGeneralService.addRoute(this.routeCode, this.start, this.dest).subscribe(
            () => this.goToRouteListBreadcrumb(),
            (error => {
                if (error.error === 'ERROR.ROUTE_START_DESTINATION_UNIQUE') {
                    this.startDestinationUniqueViolation = true;
                } else if (error.error === 'ERROR.ROUTE_CODE_UNIQUE') {
                    this.codeUniqueViolation = true;
                } else {
                    this.errorHandlerService.handleError(error);
                }
            })
        );
    }

    getSeaports(): SeaportGeneral[] {
        return this.seaportGeneralService.seaportsList.filter(
            seaport => seaport.code !== this.start && seaport.code !== this.dest
        );
    }

    getCity(code: string): string | undefined {
        return this.seaportGeneralService.seaportsList.find(seaport => seaport.code === code)?.city;
    }

    private clearErrors(): void {
        this.codeUniqueViolation = false;
        this.startDestinationUniqueViolation = false;
    }
}
