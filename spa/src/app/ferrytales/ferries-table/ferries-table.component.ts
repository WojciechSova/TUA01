import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FerryGeneral } from '../../model/mop/FerryGeneral';
import { FerryGeneralService } from '../../services/mop/ferry-general.service';

@Component({
    selector: 'app-ferries-table',
    templateUrl: './ferries-table.component.html',
    styleUrls: ['./ferries-table.component.less']
})
export class FerriesTableComponent implements OnInit {

    constructor(private router: Router,
                private ferryGeneralService: FerryGeneralService) {
        this.getFerries();
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    getFerries(): void {
        this.ferryGeneralService.getFerries().subscribe(
            (response: FerryGeneral[]) => {
                this.ferryGeneralService.ferriesGeneralList = response;
            }
        );
    }

    listFerries(): FerryGeneral[] {
        return this.ferryGeneralService.ferriesGeneralList;
    }

    goToFerryDetails(name: string): void {
        this.router.navigate(['/ferrytales/ferries/', name]);
    }
}
