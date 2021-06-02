import { Injectable, OnDestroy } from "@angular/core";
import { FerryGeneral } from "../model/mop/FerryGeneral";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class FerryGeneralService implements OnDestroy {

    ferriesGeneralList: FerryGeneral[] = [];

    constructor(private http: HttpClient) {
    }

    getFerries(): any {

    }

    ngOnDestroy(): void {
        this.ferriesGeneralList = [];
    }


}
