import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BookingGeneralService } from '../../services/mop/booking-general.service';
import { IdentityService } from '../../services/utils/identity.service';
import { BookingGeneral } from '../../model/mop/BookingGeneral';

@Component({
    selector: 'app-bookings-table-own',
    templateUrl: './bookings-table-own.component.html',
    styleUrls: ['./bookings-table-own.component.less']
})
export class BookingsTableOwnComponent implements OnInit {

    bookingToCancel = '';
    bookingCancelError = false;
    isPromptVisible = false;
    bookingCancelSuccess = false;

    constructor(private router: Router,
                public bookingGeneralService: BookingGeneralService,
                public identityService: IdentityService) {
        this.getBookings();
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
        this.bookingGeneralService.popup = 'hidden';
        this.router.navigate(['/']);
    }

    getBookings(): void {
        this.bookingGeneralService.getOwnBookings().subscribe(
            (bookingGenerals: BookingGeneral[]) => {
                this.bookingGeneralService.readBookings(bookingGenerals);
            }
        );
    }

    showBookingDetails(bookingNumber: string): void {
        this.bookingGeneralService.popup = 'hidden';
        this.router.navigate(['/ferrytales/bookings/own/' + bookingNumber]);
    }

    displayPrompt(bookingNumber: string): void {
        this.bookingCancelError = false;
        this.bookingToCancel = bookingNumber;
        this.isPromptVisible = true;
    }

    cancelBooking(bookingNumber: string): void {
        this.bookingCancelError = false;
        this.bookingCancelSuccess = false;
        this.bookingGeneralService.popup = 'hidden';
        this.bookingGeneralService.remove(bookingNumber).subscribe(
            () => {
                this.getBookings();
                this.bookingCancelSuccess = true;
            },
            (error => {
                if (error.error === 'ERROR.CANNOT_CANCEL_BOOKING') {
                    this.bookingCancelError = true;
                }
            })
        );
    }

    getConfirmationResult(confirmationResult: boolean): void {
        if (confirmationResult) {
            this.cancelBooking(this.bookingToCancel);
        }
        this.isPromptVisible = false;
    }
}
