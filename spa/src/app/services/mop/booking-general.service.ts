import { Injectable, OnDestroy } from '@angular/core';
import { BookingGeneral } from '../../model/mop/BookingGeneral';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class BookingGeneralService implements OnDestroy {

    public bookings: BookingGeneral[];
    private readonly url: string;
    popup = 'hidden';

    constructor(private http: HttpClient) {
        this.url = environment.appUrl + '/bookings';
        this.bookings = [];
    }

    private static parseDates(bookings: BookingGeneral[]): BookingGeneral[] {
        bookings.forEach(booking => booking.creationDate = this.parseDate(booking.creationDate));
        bookings.forEach(booking => booking.cruise.startDate = this.parseDate(booking.cruise.startDate));
        bookings.forEach(booking => booking.cruise.endDate = this.parseDate(booking.cruise.endDate));
        return bookings;
    }

    private static parseDate(stringDate: any): any {
        if (!stringDate) {
            return undefined;
        }
        return new Date(stringDate.toString().split('[UTC]')[ 0 ]);
    }

    getBookings(): Observable<BookingGeneral[]> {
        return this.http.get<BookingGeneral[]>(this.url, {
            observe: 'body',
            responseType: 'json'
        });
    }

    getOwnBookings(): Observable<BookingGeneral[]> {
        return this.http.get<BookingGeneral[]>(this.url + '/own', {
            observe: 'body',
            responseType: 'json'
        });
    }

    readBookings(bookings: BookingGeneral[]): void {
        this.bookings = bookings;
        this.bookings = BookingGeneralService.parseDates(this.bookings);
    }

    remove(bookingNumber: string): Observable<any> {
        return this.http.delete(this.url.concat('/cancel/', bookingNumber), {
            observe: 'body',
            responseType: 'text'
        });
    }

    ngOnDestroy(): void {
        this.bookings = [];
        this.popup = 'hidden';
    }
}
