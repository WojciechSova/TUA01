import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validateEmail, validatePassword } from './matching.validator';
import { RegistrationService } from '../../../services/registration.service';
import { AccountDetails } from '../../../model/mok/AccountDetails';
import { getTimezone } from 'countries-and-timezones';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.less']
})
export class RegisterComponent implements OnInit {

    private registrationService: RegistrationService;

    constructor(registrationService: RegistrationService) {
        this.registrationService = registrationService;
    }

    @Output()
    isLoginVisibleChange = new EventEmitter<boolean>();

    @Output()
    isRegisterVisibleChange = new EventEmitter<boolean>();

    form = new FormGroup({
        login: new FormControl('', Validators.required),
        password: new FormControl('', [Validators.required, Validators.minLength(8), validatePassword]),
        passwordRepeat: new FormControl('', [Validators.required, validatePassword]),
        firstName: new FormControl('', Validators.required),
        lastName: new FormControl('', Validators.required),
        email: new FormControl('', [Validators.required, Validators.email, validateEmail]),
        emailRepeat: new FormControl('', [Validators.required, validateEmail]),
        phoneNumber: new FormControl('', Validators.pattern('[0-9]{3,15}'))
    });

    error = false;

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.isLoginVisibleChange.emit(false);
        this.isRegisterVisibleChange.emit(false);
    }

    openLogin(): void {
        this.isLoginVisibleChange.emit(true);
        this.isRegisterVisibleChange.emit(false);
    }

    register(login: string, password: string, firstName: string, lastName: string, email: string, phoneNumber?: string): void {
        const account: AccountDetails = {
            login,
            password,
            firstName,
            lastName,
            email,
            phoneNumber: phoneNumber === '' ? undefined : phoneNumber,
            active: true,
            confirmed: false,
            accessLevel: [
                {
                    level: 'CLIENT',
                    active: true,
                    creationDate: new Date()
                },
            ],
            language: navigator.language || window.navigator.language,
            timeZone: getTimezone(Intl.DateTimeFormat().resolvedOptions().timeZone)?.utcOffsetStr,
            creationDate: new Date(),
            numberOfBadLogins: 0
        };
        this.registrationService.register(account).subscribe(
            () => this.closeComponent(),
            (error: any) => this.error = true
        );
    }
}
