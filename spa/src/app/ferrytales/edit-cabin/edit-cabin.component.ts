import {Component, OnInit} from '@angular/core';
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

    public cabinTypes: string[] = [
        'First class',
        'Second class',
        'Third class',
        'Disabled class'
    ];

    form = new FormGroup({
        capacity: new FormControl('')
    });

    private updating = false;

    constructor(private router: Router,
                public identityService: IdentityService,
                public cabinDetailsService: CabinDetailsService) {
    }

    ngOnInit(): void {
    }

    isUpdating(): boolean {
        return this.updating;
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerriesListBreadcrumb(): void {

    }

    goToFerryBreadcrumb(): void {

    }

    goToCabinBreadcrumb(): void {

    }

    showCabin(cabinNumber: string): void {

    }

    editCabin(value: string): void {

    }
}
