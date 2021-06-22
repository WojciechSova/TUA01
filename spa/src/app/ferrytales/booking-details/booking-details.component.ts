import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IdentityService } from '../../services/utils/identity.service';
import { BookingDetails } from '../../model/mop/BookingDetails';
import { BookingDetailsService } from '../../services/mop/booking-details.service';

@Component({
    selector: 'app-booking-details',
    templateUrl: './booking-details.component.html',
    styleUrls: ['./booking-details.component.less']
})
export class BookingDetailsComponent implements OnInit {

    number = '';

    constructor(private route: ActivatedRoute,
                private router: Router,
                public identityService: IdentityService,
                public bookingDetailsService: BookingDetailsService) {
        this.number = this.route.snapshot.paramMap.get('number') as string;
        this.getBooking();
    }

    ngOnInit(): void {
    }

    getBooking(): void {
        if (this.identityService.isEmployee()) {
            this.bookingDetailsService.getBooking(this.number).subscribe(
                (response: BookingDetails) => {
                    this.bookingDetailsService.readBooking(response);
                }
            );
        } else {
            this.bookingDetailsService.getOwnBooking(this.number).subscribe(
                (response: BookingDetails) => {
                    this.bookingDetailsService.readBooking(response);
                }
            );
        }
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToBookingListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/bookings']);
    }

    goToOwnBookingListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/my/bookings']);
    }

    translateCabinType(cabinType: string | undefined): string {
        if (cabinType === 'First class') {
            return 'cabin-form.first';
        }
        if (cabinType === 'Second class') {
            return 'cabin-form.second';
        }
        if (cabinType === 'Third class') {
            return 'cabin-form.third';
        }
        return 'cabin-form.disabled';
    }
}
