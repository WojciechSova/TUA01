import { Component, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { RouteGeneralService } from '../../services/mop/route-general.service';
import { RouteGeneral } from '../../model/mop/RouteGeneral';

@Component({
    selector: 'app-routes-table',
    templateUrl: './routes-table.component.html',
    styleUrls: ['./routes-table.component.less']
})
export class RoutesTableComponent implements OnDestroy {

    result = 'hidden';

    routes: RouteGeneral[] = [];

    constructor(private router: Router,
                private routeGeneralService: RouteGeneralService) {
        this.getRoutes();
    }

    goToHomepageBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    getRoutes(): void {
        this.routeGeneralService.getAllRoutes().subscribe((response: RouteGeneral[]) => {
            this.routes = response;
        });
    }

    addRoute(): void {
        this.router.navigate(['/ferrytales/routes/add']);
    }

    refresh(): void {
        this.getRoutes();
        this.result = 'hidden';
    }

    removeRoute(code: string): void {
        this.result = 'hidden';
        this.routeGeneralService.removeRoute(code).subscribe(
            () => {
                this.result = 'success';
                this.getRoutes();
            },
            (error: any) => {
                if (error.status === 409) {
                    this.result = 'failure';
                }
                this.getRoutes();
            });
    }

    ngOnDestroy(): void {
        this.routes = [];
    }
}
