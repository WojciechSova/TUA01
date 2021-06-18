import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SeaportGeneral } from '../../model/mop/SeaportGeneral';
import { SeaportGeneralService } from '../../services/mop/seaport-general.service';

@Component({
    selector: 'app-seaports-table',
    templateUrl: './seaports-table.component.html',
    styleUrls: ['./seaports-table.component.less']
})
export class SeaportsTableComponent implements OnInit {

    deleteResultMessage = 'HIDDEN';

    constructor(private router: Router,
                private seaportGeneralService: SeaportGeneralService) {
        this.getSeaports();
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    refresh(): void {
        this.deleteResultMessage = 'HIDDEN';
        this.getSeaports();
    }

    getSeaports(): void {
        this.seaportGeneralService.getSeaports().subscribe(
            (response: SeaportGeneral[]) => {
                this.seaportGeneralService.seaportsList = response;
            }
        );
    }

    listSeaports(): SeaportGeneral[] {
        return this.seaportGeneralService.seaportsList;
    }

    showAddingForm(): void {
        this.router.navigate(['/ferrytales/seaports/add']);
    }

    deleteSeaport(code: string): void {
        this.seaportGeneralService.deleteSeaport(code).subscribe(() => {
            this.deleteResultMessage = 'SUCCESS';
        }, () => {
            this.deleteResultMessage = 'FAILURE';
        });
        this.getSeaports();
    }
}
