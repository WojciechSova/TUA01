import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RouteGeneralService } from '../../services/mop/route-general.service';
import { RouteGeneral } from '../../model/mop/RouteGeneral';

@Component({
    selector: 'app-routes-table',
    templateUrl: './routes-table.component.html',
    styleUrls: ['./routes-table.component.less']
})
export class RoutesTableComponent implements OnInit {

    // routes: RouteGeneral[] = [];
    routes: RouteGeneral[] = [ {
            start: 'starting location',
            destination: 'destination location',
            code: 'code of route'
        }, {
        start: 'more',
        destination: 'more',
        code: 'more'
    }
    ];

    constructor(private router: Router,
                private routeGeneralService: RouteGeneralService) {
    }

    ngOnInit(): void {

    }

    goToHomepageBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    getRoutes(): void {
        // TODO
    }

    addRoute(): void {
        // TODO
    }
}
