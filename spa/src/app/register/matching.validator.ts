import {AbstractControl} from '@angular/forms';

export function validatePassword(control: AbstractControl): { [key: string]: boolean } | null {


    const passwordPath = 'password';
    const passwordRepeatPath = 'passwordRepeat';

    return checkMatching(control, passwordPath, passwordRepeatPath);

}

export function validateEmail(control: AbstractControl): { [key: string]: boolean } | null {

    const emailPath = 'email';
    const emailRepeatPath = 'emailRepeat';

    return checkMatching(control, emailPath, emailRepeatPath);
}

function checkMatching(control: AbstractControl, controlPath: string, controlRepeatPath: string): { [key: string]: boolean } | null{
    let controlToCompare: AbstractControl | null;
    if (control === control.parent?.get(controlPath)) {

        controlToCompare = control.parent?.get(controlRepeatPath);

        if (controlToCompare && controlToCompare.value !== control.value) {
            controlToCompare.setErrors({notMatching: true});

            return null;
        }
        else if (controlToCompare && controlToCompare.value === control.value) {

            delete controlToCompare.errors?.notMatching;
            if (controlToCompare.errors && Object.keys(controlToCompare.errors).length === 0) {
                controlToCompare.setErrors(null);
            }
            return null;
        }

    } else if (control === control.parent?.get(controlRepeatPath)) {
        controlToCompare = control.parent?.get(controlPath);

        if (controlToCompare && controlToCompare.value !== control.value) {
            return {notMatching: true};
        }
        else if (controlToCompare && controlToCompare.value === control.value) {
            return null;
        }
    }
    return  null;
}
