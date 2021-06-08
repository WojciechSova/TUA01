import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../environments/environment";
import { RouteDetails } from "../../model/mop/RouteDetails";

@Injectable({
    providedIn: 'root'
})
export class RouteDetailsService {

    private readonly url: string;

    routeDetails: RouteDetails = {
        start: {
            city: '',
            code: '',
        },
        destination: {
            city: '',
            code: '',
        },
        code: '',
        cruises: [],
        creationDate: new Date(),
        createdBy: {
            login: '',
            active: true,
            firstName: '',
            lastName: '',
            accessLevel: []
        }
    };


    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/routes';
    }

    getRouteAndCruisesForRoute(code: string): any {
        return this.http.get<any>(this.url + '/' + code, {
            observe: 'body',
            responseType: 'json'
        });
    }

    readRouteDetails(response: RouteDetails): void {
        this.routeDetails = response;
        this.routeDetails = this.parseDates(this.routeDetails);
    }

    private parseDates(routeDetails: RouteDetails): RouteDetails {
        routeDetails.creationDate = this.parseDate(routeDetails.creationDate);
        routeDetails.cruises.forEach(cruise => cruise.endDate = this.parseDate(cruise.endDate));
        routeDetails.cruises.forEach(cruise => cruise.startDate = this.parseDate(cruise.startDate));
        return routeDetails;
    }

    private parseDate(stringDate: any): any {
        if (!stringDate) {
            return undefined;
        }
        return new Date(stringDate.toString().split('[UTC]')[ 0 ]);
    }

    ngOnDestroy(): void {
        this.routeDetails = {
            start: {
                city: '',
                code: '',
            },
            destination: {
                city: '',
                code: '',
            },
            code: '',
            cruises: [],
            creationDate: new Date(),
            createdBy: {
                login: '',
                active: true,
                firstName: '',
                lastName: '',
                accessLevel: []
            }
        }
    }
}
