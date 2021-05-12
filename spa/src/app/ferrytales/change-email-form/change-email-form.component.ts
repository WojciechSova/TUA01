import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {validateEmail} from '../../common/navigation/register/matching.validator';


@Component({
    selector: 'app-change-email-form',
    templateUrl: './change-email-form.component.html',
    styleUrls: ['./change-email-form.component.less']
})
export class ChangeEmailFormComponent implements OnInit {

    @Output()
    isChangeEmailFormVisible = new EventEmitter<boolean>();

    form = new FormGroup({
        email: new FormControl('', [Validators.required, Validators.email, validateEmail]),
        emailRepeat: new FormControl('', [Validators.required, validateEmail]),
    });

    constructor() {
    }

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.isChangeEmailFormVisible.emit(false);
    }

}
