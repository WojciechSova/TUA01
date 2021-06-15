import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Router} from '@angular/router';
import {IdentityService} from '../../services/utils/identity.service';
import {CabinDetailsService} from '../../services/mop/cabin-details.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
    selector: 'app-edit-cabin',
    templateUrl: './edit-cabin.component.html',
    styleUrls: ['./edit-cabin.component.less']
})
export class EditCabinComponent implements OnInit {

    public cabinTypes = {
        firstClass: false,
        secondClass: false,
        thirdClass: false,
        disabledClass: false
    };

    form = new FormGroup({
        capacity: new FormControl('')
    });

    private updating = false;

    constructor(private router: Router,
                public identityService: IdentityService,
                public cabinDetailsService: CabinDetailsService) {
    }

    ngOnInit(): void {
        if (this.cabinDetailsService.cabin.cabinType === 'First class') {
            this.cabinTypes.firstClass = true;
        } else if (this.cabinDetailsService.cabin.cabinType === 'Second class') {
            this.cabinTypes.secondClass = true;
        } else if (this.cabinDetailsService.cabin.cabinType === 'Third class') {
            this.cabinTypes.thirdClass = true;
        } else if (this.cabinDetailsService.cabin.cabinType === 'Disabled class') {
            this.cabinTypes.disabledClass = true;
        }
    }

    isUpdating(): boolean {
        return this.updating;
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerriesListBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerryBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToCabinBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    showCabin(cabinNumber: string): void {

    }

    editCabin(value: string): void {
        console.log(this.cabinTypes);
    }
}
