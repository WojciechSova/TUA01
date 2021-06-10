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
            firstName: 'First name',
            lastName: 'Last Name',
            accessLevel: ['']
        },
        cabin: {
            capacity: 'capacity',
            cabinType: 'cabin type',
            number: '123'
        },
        creationDate: new Date(),
        cruise: {
            startDate: new Date(),
            endDate: new Date(),
            route: {
                start: {
                    code: '123',
                    city: 'A'
                },
                destination: {
                    code: '1234',
                    city: 'B'
                },
                code: '123'
            },
            ferry: {
                name: '123',
                onDeckCapacity: 123,
                vehicleCapacity: 123
            },
            number: '123',
            modificationDate: new Date(),
            creationDate: new Date(),
            createdBy: {
                login: 'Login',
                active: true,
                firstName: '',
                lastName: '',
                accessLevel: ['']
            },
        },
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
                firstName: 'First name',
                lastName: 'Last name',
                accessLevel: ['']
            },
            cabin: {
                capacity: 'capacity',
                cabinType: 'cabin type',
                number: '123'
            },
            creationDate: new Date(),
            cruise: {
                startDate: new Date(),
                endDate: new Date(),
                route: {
                    start: {
                        code: '123',
                        city: 'A'
                    },
                    destination: {
                        code: '1234',
                        city: 'B'
                    },
                    code: '123'
                },
                ferry: {
                    name: '123',
                    onDeckCapacity: 123,
                    vehicleCapacity: 123
                },
                number: '123',
                modificationDate: new Date(),
                creationDate: new Date(),
                createdBy: {
                    login: 'Login',
                    active: true,
                    firstName: '',
                    lastName: '',
                    accessLevel: ['']
                },
            },
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
