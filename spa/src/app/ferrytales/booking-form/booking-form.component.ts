import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingDetailsService } from '../../services/mop/booking-details.service';
import { IdentityService } from '../../services/utils/identity.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CruiseDetailsService } from '../../services/mop/cruise-details.service';
import { HttpResponse } from '@angular/common/http';
import { AccountDetails } from '../../model/mok/AccountDetails';
import { AccountDetailsService } from '../../services/mok/account-details.service';
import { CabinGeneralService } from '../../services/mop/cabin-general.service';
import { CabinGeneral } from '../../model/mop/CabinGeneral';
import { ErrorHandlerService } from '../../services/error-handlers/error-handler.service';
import { BookingGeneralService } from '../../services/mop/booking-general.service';

@Component({
    selector: 'app-booking-form',
    templateUrl: './booking-form.component.html',
    styleUrls: ['./booking-form.component.less']
})
export class BookingFormComponent implements OnInit {

    @ViewChild('disabledClass')
    private disabledClass: ElementRef;

    isPromptVisible = false;

    public cabinList: CabinGeneral[] = [];

    public filteredCabins: CabinGeneral[] = [];

    public selectedCabin: CabinGeneral | undefined;

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
    noCabinsAvailable = false;
    wantCabin = false;

    errorConstraint = false;
    cruiseStarted = false;
    notEnoughVehicleSpace = false;
    notEnoughCabinSpace = false;
    cabinOccupied = false;
    notEnoughFerrySpace = false;

    currentPrice = 0.0;

    currentCapacity = '';
    selectedNumber = '';
    currentPeopleNumber = '';
    cruiseNumber = '';
    currentType = '';

    bookingForm = new FormGroup({
        numberOfPeople: new FormControl('', [Validators.required, Validators.pattern('[1-9][0-9]*')])
    });

    constructor(private router: Router,
                private route: ActivatedRoute,
                public bookingDetailsService: BookingDetailsService,
                public identityService: IdentityService,
                public cruiseDetailsService: CruiseDetailsService,
                public accountDetailsService: AccountDetailsService,
                private cabinGeneralService: CabinGeneralService,
                private errorHandlerService: ErrorHandlerService,
                private bookingGeneralService: BookingGeneralService) {
        this.disabledClass = new ElementRef('disabledClass');
        this.cruiseNumber = this.route.snapshot.paramMap.get('number') as string;
        this.getCruise();
        this.getAccount();
        this.getFreeCabins();
    }

    ngOnInit(): void {
    }


    changeWantCabin(want: string): void {
        this.wantCabin = want === 'yes';
        this.cabinTypeChosen = false;
        this.cabinTypes.firstClass = false;
        this.cabinTypes.secondClass = false;
        this.cabinTypes.thirdClass = false;
        this.cabinTypes.disabledClass = false;
        this.disabledClass.nativeElement.style.cssText = 'opacity: 0.6;';
        this.calculatePrice();
    }

    chooseVehicleType(value: string): void {
        this.vehicleTypes.none = false;
        this.vehicleTypes.bike = false;
        this.vehicleTypes.car = false;
        this.vehicleTypes.bus = false;
        if (value === 'none') {
            this.vehicleTypes.none = true;
        } else if (value === 'bike') {
            this.vehicleTypes.bike = true;
        } else if (value === 'car') {
            this.vehicleTypes.car = true;
        } else if (value === 'bus') {
            this.vehicleTypes.bus = true;
        }
        this.calculatePrice();
    }

    chooseCabinType(value: string): void {
        this.selectedNumber = '';
        this.cabinTypeChosen = true;
        this.cabinTypes.firstClass = false;
        this.cabinTypes.secondClass = false;
        this.cabinTypes.thirdClass = false;
        this.cabinTypes.disabledClass = false;
        if (value === 'Disabled class') {
            this.disabledClass.nativeElement.style.cssText = 'opacity: 1;';
            this.cabinTypes.disabledClass = true;
            this.currentType = 'Disabled class';
        } else if (value === 'First class') {
            this.disabledClass.nativeElement.style.cssText = 'opacity: 0.6;';
            this.cabinTypes.firstClass = true;
            this.currentType = 'First class';
        } else if (value === 'Second class') {
            this.disabledClass.nativeElement.style.cssText = 'opacity: 0.6;';
            this.cabinTypes.secondClass = true;
            this.currentType = 'Second class';
        } else if (value === 'Third class') {
            this.disabledClass.nativeElement.style.cssText = 'opacity: 0.6;';
            this.cabinTypes.thirdClass = true;
            this.currentType = 'Third class';
        }
        this.filterFreeCabins(this.currentType);
        this.calculatePrice();
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToOwnBookings(): void {
        this.router.navigate(['/ferrytales/my/bookings']);
    }

    goToCurrentCruises(): void {
        this.router.navigate(['/ferrytales/current']);
    }

    getCruise(): void {
        this.cruiseDetailsService.getCruise(this.cruiseNumber).subscribe(
            (response) => this.cruiseDetailsService.readCruiseAndEtagFromResponse(response)
        );
    }

    getFreeCabins(): void {
        this.cabinGeneralService.getFreeCabinsOnCruise(this.cruiseNumber).subscribe(
        (response: CabinGeneral[]) => {
            this.cabinList = response;
        });
    }

    filterFreeCabins(type: string): void {
        this.selectedNumber = '';
        const people = parseInt(this.currentPeopleNumber, 10);
        this.filteredCabins = [];
        this.noCabinsAvailable = false;
        for (const cabin of this.cabinList) {
            if (cabin.cabinType === type && parseInt(cabin.capacity, 10) === people){
                this.filteredCabins.push(cabin);
            }
        }
        if (this.filteredCabins.length === 0){
            for (const cabin of this.cabinList) {
                if (cabin.cabinType === type && parseInt(cabin.capacity, 10) > people){
                    this.filteredCabins.push(cabin);
                }
            }
        }

        if (this.filteredCabins.length === 0){
            this.noCabinsAvailable = true;
        }

        this.calculatePrice();
    }

    getAccount(): void {
        this.accountDetailsService.getProfile().subscribe(
            (response: HttpResponse<AccountDetails>) => {
                this.accountDetailsService.readAccountAndEtagFromResponse(response);
            });
    }

    calculatePrice(): void {
        this.currentPrice = 0.0;
        const people = parseInt(this.selectedCabin?.capacity as string, 10);
        if (this.wantCabin){
            if (this.currentType === 'Disabled class'){
                this.currentPrice += 100 * people;
            }
            if (this.currentType === 'Third class'){
                this.currentPrice += 80 * people;
            }
            if (this.currentType === 'Second class'){
                this.currentPrice += 150 * people;
            }
            if (this.currentType === 'First class'){
                this.currentPrice += 200 * people;
            }
        }
        else {
            this.currentPrice += 30 * parseInt(this.currentPeopleNumber, 10);
        }
        if (this.vehicleTypes.bike){
            this.currentPrice += 50;
        }
        if (this.vehicleTypes.car){
            this.currentPrice += 100;
        }
        if (this.vehicleTypes.bus){
            this.currentPrice += 250;
        }

        if (isNaN(this.currentPrice) || (this.selectedNumber.length < 4 && this.wantCabin) || (this.noCabinsAvailable && this.wantCabin)){
            this.currentPrice = 0.0;
        }
    }


    selectCabin(): void{
        for (const cabin of this.filteredCabins) {
            if (cabin.number === this.selectedNumber){
                this.selectedCabin = cabin;
                this.calculatePrice();
            }
        }
    }

    displayPrompt(): void {
        this.isPromptVisible = true;
    }

    getConfirmationResult(confirmationResult: any): void {
        if (confirmationResult) {
            this.createBooking();
        }
        this.isPromptVisible = false;
    }

    createBooking(): void{
        this.errorConstraint = false;
        this.cruiseStarted = false;
        this.notEnoughVehicleSpace = false;
        this.notEnoughCabinSpace = false;
        this.cabinOccupied = false;
        this.notEnoughFerrySpace = false;
        const people = parseInt(this.currentPeopleNumber, 10);
        let vehicleTypeName = 'None';
        if (this.vehicleTypes.bike){
            vehicleTypeName = 'Motorcycle';
        }
        if (this.vehicleTypes.car){
            vehicleTypeName = 'Car';
        }
        if (this.vehicleTypes.bus){
            vehicleTypeName = 'Bus';
        }
        this.bookingDetailsService.addBooking(people, this.cruiseNumber, this.selectedNumber, vehicleTypeName).subscribe(
            () => {
                this.goToOwnBookings();
                this.bookingGeneralService.popup = 'add-success';
            },
            (error: any) => {
                if (error.error === 'ERROR.CRUISE_ALREADY_STARTED') {
                    this.cruiseStarted = true;
                } else if (error.error === 'ERROR.FERRY_NOT_ENOUGH_SPACE_FOR_VEHICLE_ON_FERRY') {
                    this.notEnoughVehicleSpace = true;
                } else if (error.error === 'ERROR.CABIN_CAPACITY_LESS_THAN_PEOPLE_NUMBER') {
                    this.notEnoughCabinSpace = true;
                } else if (error.error === 'ERROR.CABIN_OCCUPIED') {
                    this.cabinOccupied = true;
                } else if (error.error === 'ERROR.FERRY_CAPACITY_LESS_THAN_PEOPLE_NUMBER') {
                    this.notEnoughFerrySpace = true;
                } else if (error.status === 409){
                    this.errorConstraint = true;
                } else {
                    this.errorHandlerService.handleError(error);
                }
            }
        );
    }

}
