import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IdentityService } from '../../services/utils/identity.service';
import { BookingDetails } from '../../model/mop/BookingDetails';

@Component({
    selector: 'app-booking-details',
    templateUrl: './booking-details.component.html',
    styleUrls: ['./booking-details.component.less']
})
export class BookingDetailsComponent implements OnInit {

    number = '';

    booking: BookingDetails = {
        account: {
            login: 'Login',
            active: true,
            firstName: '',
            lastName: '',
            accessLevel: ['']
        },
        cabin: 'cabin',
        creationDate: new Date(),
        cruise: 'Cruise',
        number: '1234567890',
        numberOfPeople: 1,
        price: 23.43,
        vehicleType: {
            vehicleTypeName: 'Name',
            requiredSpace: 1
        }
    };

    constructor(private route: ActivatedRoute,
                private router: Router,
                public identityService: IdentityService) {
        this.number = this.route.snapshot.paramMap.get('number') as string;
        this.getBooking();
    }

    ngOnInit(): void {
    }

    getBooking(): void {
        this.booking = {
            account: {
                login: 'Login',
                active: true,
                firstName: '',
                lastName: '',
                accessLevel: ['']
            },
            cabin: 'cabin',
            creationDate: new Date(),
            cruise: 'Cruise',
            number: '1234567890',
            numberOfPeople: 1,
            price: 23.43,
            vehicleType: {
                vehicleTypeName: 'Name',
                requiredSpace: 1
            }
        };
    }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToBookingListBreadcrumb(): void {
        this.router.navigate(['/ferrytales/bookings']);
    }

}
