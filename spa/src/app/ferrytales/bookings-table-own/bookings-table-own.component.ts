import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {BookingGeneralService} from '../../services/mop/booking-general.service';
import {IdentityService} from '../../services/utils/identity.service';

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

    }

    showBookingDetails(bookingNumber: string): void {
        return;
    }

}
