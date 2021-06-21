import { Injectable, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { BookingDetails } from '../../model/mop/BookingDetails';

@Injectable({
    providedIn: 'root'
})
export class BookingDetailsService implements OnDestroy {

    booking: BookingDetails;

    private readonly url: string;

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/bookings/';
        this.booking = this.getDefaultBookingDetails();
    }

    private parseDates(booking: BookingDetails): BookingDetails {
        booking.creationDate = this.parseDate(booking.creationDate);
        booking.cruise.startDate = this.parseDate(booking.cruise.startDate);
        booking.cruise.endDate = this.parseDate(booking.cruise.endDate);
        return booking;
    }

    private parseDate(stringDate: any): any {
        if (!stringDate) {
            return undefined;
        }
        return new Date(stringDate.toString().split('[UTC]')[0]);
    }

    getBooking(bookingNumber: string): Observable<BookingDetails> {
        return this.http.get<BookingDetails>(this.url + bookingNumber, {
            observe: 'body',
            responseType: 'json'
        });
    }

    addBooking(peopleNumber: number, cruiseNumber: string, cabinNumber: string, vehicleTypeName: string): any{
        return this.http.post(this.url + 'add/' + cruiseNumber + '/' + vehicleTypeName + '/' + cabinNumber, peopleNumber, {
            observe: 'body',
            responseType: 'json'
            });
    }

    getOwnBooking(bookingNumber: string): Observable<BookingDetails> {
        return this.http.get<BookingDetails>(this.url + 'own/' + bookingNumber, {
            observe: 'body',
            responseType: 'json'
        });
    }

    readBooking(booking: BookingDetails): void {
        this.booking = booking;
        this.booking = this.parseDates(this.booking);
    }

    ngOnDestroy(): void {
        this.booking = this.getDefaultBookingDetails();
    }

    private getDefaultBookingDetails(): BookingDetails {
        return  {
            account: {
                login: '',
                active: false,
                firstName: '',
                lastName: '',
                accessLevel: ['']
            },
            cabin: {
                capacity: '',
                cabinType: '',
                number: ''
            },
            creationDate: new Date(),
            cruise: {
                startDate: new Date(),
                endDate: new Date(),
                route: {
                    start: {
                        code: '',
                        city: ''
                    },
                    destination: {
                        code: '',
                        city: ''
                    },
                    code: ''
                },
                ferry: {
                    name: '',
                    onDeckCapacity: 0,
                    vehicleCapacity: 0
                },
                number: '',
                modificationDate: new Date(),
                creationDate: new Date(),
                createdBy: {
                    login: '',
                    active: false,
                    firstName: '',
                    lastName: '',
                    accessLevel: ['']
                },
            },
            number: '',
            numberOfPeople: 0,
            price: 0,
            vehicleType: {
                vehicleTypeName: '',
                requiredSpace: 0
            }
        } as BookingDetails;
    }
}
