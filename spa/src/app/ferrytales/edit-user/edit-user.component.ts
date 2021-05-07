import { Component, OnInit } from '@angular/core';
import { AccountDetails } from "../../model/mok/AccountDetails";
import { FormControl, FormGroup, Validators } from "@angular/forms";

@Component({
    selector: 'app-edit-user',
    templateUrl: './edit-user.component.html',
    styleUrls: ['./edit-user.component.less']
})
export class EditUserComponent implements OnInit {

    public user: AccountDetails = {
        accessLevel: [],
        active: false,
        confirmed: false,
        creationDate: new Date(),
        email: '',
        firstName: '',
        lastName: '',
        numberOfBadLogins: 0,
        login: '',
        password: '',
        phoneNumber: '',
        language: '',
        timeZone: ''
    };

    public timezones: string[] = [
        'GMT-12',
        'GMT-11',
        'GMT-10',
        'GMT-9',
        'GMT-8',
        'GMT-7',
        'GMT-6',
        'GMT-5',
        'GMT-4',
        'GMT-3',
        'GMT-2',
        'GMT-1',
        'GMT+0',
        'GMT+1',
        'GMT+2',
        'GMT+3',
        'GMT+4',
        'GMT+5',
        'GMT+6',
        'GMT+7',
        'GMT+8',
        'GMT+9',
        'GMT+10',
        'GMT+11',
        'GMT+12'
    ]

    form = new FormGroup({
        firstName: new FormControl(''),
        lastName: new FormControl(''),
        phoneNumber: new FormControl('', [Validators.pattern('[0-9]{11}')]),
        language: new FormControl(''),
        timeZone: new FormControl('')
    });

    constructor() {
    }

    editUser(firstName?: string, lastName?: string, phoneNumber?: string, language?: string, timeZone?: string): void {
    }

    ngOnInit(): void {
    }

}
