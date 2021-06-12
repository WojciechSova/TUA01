import { Component, Input } from '@angular/core';
import { IdentityService } from '../../../services/utils/identity.service';
import { CabinGeneral } from '../../../model/mop/CabinGeneral';
import { Router } from "@angular/router";

@Component({
    selector: 'app-cabin-table',
    templateUrl: './cabin-table.component.html',
    styleUrls: ['./cabin-table.component.less']
})
export class CabinTableComponent {

    constructor(public identityService: IdentityService,
                private router: Router) {
    }

    @Input()
    cabins: CabinGeneral[] = [];

    @Input()
    ferry: String = '';

    goToCabinDetails(cabinName: String): void {
        this.router.navigate(['/ferrytales/ferries/', this.ferry, cabinName]);
    }

}
