import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BookingGeneralService } from '../../services/mop/booking-general.service';
import { IdentityService } from '../../services/utils/identity.service';
import { BookingGeneral } from '../../model/mop/BookingGeneral';

@Component({
    selector: 'app-booking-table',
    templateUrl: './booking-table.component.html',
    styleUrls: ['./booking-table.component.less']
})
export class BookingTableComponent implements OnInit {

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
        this.bookingGeneralService.getBookings().subscribe(
            (bookingGenerals: BookingGeneral[]) => {
                this.bookingGeneralService.readBookings(bookingGenerals);
            }
        );
    }

    showBookingDetails(bookingNumber: string): void {
        return;
    }
}
