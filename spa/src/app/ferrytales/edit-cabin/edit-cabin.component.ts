import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IdentityService } from '../../services/utils/identity.service';
import { CabinDetailsService } from '../../services/mop/cabin-details.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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
        capacity: new FormControl('', [Validators.pattern('[1-9][0-9]?')])
    });

    private updating = false;
    private cabinNumber = '';
    private ferryName = '';
    editFailed = false;

    constructor(private router: Router,
                private route: ActivatedRoute,
                public identityService: IdentityService,
                public cabinDetailsService: CabinDetailsService) {
        this.cabinNumber = (this.route.snapshot.paramMap.get('cabin') as string);
        this.ferryName = (this.route.snapshot.paramMap.get('ferry') as string);
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

    onValueChange(value: string): void{
        this.cabinTypes.firstClass = false;
        this.cabinTypes.secondClass = false;
        this.cabinTypes.thirdClass = false;
        this.cabinTypes.disabledClass = false;

        if (value === 'first') {
            this.cabinTypes.firstClass = true;
            this.cabinDetailsService.cabin.cabinType = 'First class';
        } else if (value === 'second') {
            this.cabinTypes.secondClass = true;
            this.cabinDetailsService.cabin.cabinType = 'Second class';
        } else if (value === 'third') {
            this.cabinTypes.thirdClass = true;
            this.cabinDetailsService.cabin.cabinType = 'Third class';
        } else if (value === 'disabled') {
            this.cabinTypes.disabledClass = true;
            this.cabinDetailsService.cabin.cabinType = 'Disabled class';
        }
    }

    isUpdating(): boolean {
        return this.updating;
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerriesListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/ferries']);
    }

    goToFerryBreadcrumb(): void {
        this.router.navigate(['/ferrytales/ferries/', this.ferryName]);
    }

    goToCabinBreadcrumb(): void {
        this.router.navigate(['/ferrytales/ferries/', this.ferryName, this.cabinNumber]);
    }

    editCabin(value: string): void {
        if (value != null && value !== '') {
            this.cabinDetailsService.cabin.capacity = value;
        }
        this.cabinDetailsService.updateCabin(this.cabinDetailsService.cabin, this.ferryName).subscribe(
            () => {
                this.router.navigate(['ferrytales/ferries/', this.ferryName, this.cabinNumber]);
                this.updating = true;
                this.getCabin();
            }, () => {
                    this.editFailed = true;
                    this.getCabin();
            }
        );
    }

    getCabin(): void {
        this.cabinDetailsService.getCabin(this.ferryName, this.cabinNumber).subscribe(
            (response) => this.cabinDetailsService.readCabinAndEtagFromResponse(response)
        );
    }
}
