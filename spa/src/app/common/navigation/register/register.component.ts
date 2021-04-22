import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validateEmail, validatePassword } from './matching.validator';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.less']
})
export class RegisterComponent implements OnInit {

    constructor() {
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
        phoneNumber: new FormControl('', Validators.pattern('[0-9]{11}') )
    });

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
}
