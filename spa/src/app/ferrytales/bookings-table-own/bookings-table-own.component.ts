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

    constructor(private router: Router,
                public bookingGeneralService: BookingGeneralService,
                public identityService: IdentityService) {
        this.getBookings();
    }

    ngOnInit(): void {
    }

    goToHomeBreadcrumb(): void {
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
        this.router.navigate(['/ferrytales/bookings/own/' + bookingNumber]);
    }

}
