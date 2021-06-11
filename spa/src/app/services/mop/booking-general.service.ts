import { Injectable } from '@angular/core';
import { BookingGeneral } from '../../model/mop/BookingGeneral';

@Injectable({
    providedIn: 'root'
})
export class BookingGeneralService {

    public bookings: BookingGeneral[];

    constructor() {
        this.bookings = this.getEmptyBookings();
    }

    getEmptyBookings(): BookingGeneral[] {
        return [
            {
                cruise: {
                    startDate: new Date(),
                    endDate: new Date(),
                    route: {
                        start: {
                            city: 'City1',
                            code: 'ABC'
                        },
                        destination: {
                            city: 'City2',
                            code: 'DEF'
                        },
                        code: 'ABCDEF'
                    },
                    ferry: {
                        name: 'Ferry 1',
                        onDeckCapacity: 100,
                        vehicleCapacity: 1234
                    },
                    number: 'ABCDEF000001'
                },
                account: {
                    login: 'string',
                    active: true,
                    firstName: 'Adam',
                    lastName: 'Adam',
                    accessLevel: [],
                },
                number: 'ABC0001',
                creationDate: new Date()
            },
            {
                cruise: {
                    startDate: new Date(),
                    endDate: new Date(),
                    route: {
                        start: {
                            city: 'City1',
                            code: 'ABC'
                        },
                        destination: {
                            city: 'City2',
                            code: 'DEF'
                        },
                        code: 'ABCDEF'
                    },
                    ferry: {
                        name: 'Ferry 1',
                        onDeckCapacity: 100,
                        vehicleCapacity: 1234
                    },
                    number: 'ABCDEF000001'
                },
                account: {
                    login: 'string',
                    active: true,
                    firstName: 'Adam',
                    lastName: 'Adam',
                    accessLevel: [],
                },
                number: 'ABC0002',
                creationDate: new Date()
            }
        ];
    }
}
