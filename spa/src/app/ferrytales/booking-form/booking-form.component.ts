import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { BookingDetailsService } from '../../services/mop/booking-details.service';
import { IdentityService } from '../../services/utils/identity.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-booking-form',
    templateUrl: './booking-form.component.html',
    styleUrls: ['./booking-form.component.less']
})
export class BookingFormComponent implements OnInit {

    @ViewChild('disabledClass')
    private disabledClass: ElementRef;

    public numbers: string[] = [
        'A123',
        'B123',
        'C123'
    ];

    wantCabin = false;
    vehicleTypes = {
        none: true,
        bike: false,
        car: false,
        bus: false
    };

    cabinTypes = {
        firstClass: false,
        secondClass: false,
        thirdClass: false,
        disabledClass: false
    };

    cabinTypeChosen = true;

    currentPrice = 100.00;

    bookingForm = new FormGroup({
        numberOfPeople: new FormControl('', [Validators.required, Validators.pattern('[1-9][0-9]*')])
    });

    constructor(private router: Router,
                public bookingDetailsService: BookingDetailsService,
                public identityService: IdentityService) {
        this.disabledClass = new ElementRef('disabledClass');
    }

    ngOnInit(): void {
    }


    changeWantCabin(want: string): void {
        console.log(want);
        this.wantCabin = want === 'yes';
        console.log(this.wantCabin);
        this.cabinTypeChosen = false;
        console.log(this.cabinTypeChosen);
        console.log(this.cabinTypes);
        this.cabinTypes.firstClass = false;
        this.cabinTypes.secondClass = false;
        this.cabinTypes.thirdClass = false;
        this.cabinTypes.disabledClass = false;
        this.disabledClass.nativeElement.style.cssText = 'opacity: 0.6;';
        console.log(this.cabinTypes);
    }

    chooseVehicleType(value: string): void {
        if (value === 'none') {
            this.vehicleTypes.none = true;
            this.vehicleTypes.bike = false;
            this.vehicleTypes.car = false;
            this.vehicleTypes.bus = false;
        } else if (value === 'bike') {
            this.vehicleTypes.none = false;
            this.vehicleTypes.bike = true;
            this.vehicleTypes.car = false;
            this.vehicleTypes.bus = false;
        } else if (value === 'car') {
            this.vehicleTypes.none = false;
            this.vehicleTypes.bike = false;
            this.vehicleTypes.car = true;
            this.vehicleTypes.bus = false;
        } else if (value === 'bus') {
            this.vehicleTypes.none = false;
            this.vehicleTypes.bike = false;
            this.vehicleTypes.car = false;
            this.vehicleTypes.bus = true;
        }
    }

    chooseCabinType(value: string): void {
        this.cabinTypeChosen = true;
        if (value === 'disabled') {
            this.disabledClass.nativeElement.style.cssText = 'opacity: 1;';

            this.cabinTypes.firstClass = false;
            this.cabinTypes.secondClass = false;
            this.cabinTypes.thirdClass = false;
            this.cabinTypes.disabledClass = true;

        } else if (value === 'first') {
            this.disabledClass.nativeElement.style.cssText = 'opacity: 0.6;';

            this.cabinTypes.firstClass = true;
            this.cabinTypes.secondClass = false;
            this.cabinTypes.thirdClass = false;
            this.cabinTypes.disabledClass = false;
        } else if (value === 'second') {
            this.disabledClass.nativeElement.style.cssText = 'opacity: 0.6;';

            this.cabinTypes.firstClass = false;
            this.cabinTypes.secondClass = true;
            this.cabinTypes.thirdClass = false;
            this.cabinTypes.disabledClass = false;
        } else if (value === 'third') {
            this.disabledClass.nativeElement.style.cssText = 'opacity: 0.6;';

            this.cabinTypes.firstClass = false;
            this.cabinTypes.secondClass = false;
            this.cabinTypes.thirdClass = true;
            this.cabinTypes.disabledClass = false;
        }
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }


    goToCurrentCruises(): void {
        this.router.navigate(['/ferrytales/current']);
    }
}
