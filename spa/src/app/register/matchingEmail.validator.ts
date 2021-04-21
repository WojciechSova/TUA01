import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, ValidatorFn} from '@angular/forms';


export function validateEmail(control: AbstractControl): { [key: string]: boolean } | null {

    let controlToCompare: AbstractControl | null;
    if (control === control.parent?.get('email')) {
        console.log('control to email');
        controlToCompare = control.parent?.get('emailRepeat');

        if (controlToCompare && controlToCompare.value !== control.value) {
            controlToCompare.setErrors({notMatching: true});
            console.log('ustawiono błąd na', controlToCompare.value);
            console.log(controlToCompare.hasError('notMatching'));

            return null;
        }
        else if (controlToCompare && controlToCompare.value === control.value) {
            controlToCompare.setErrors({notMatching: null});
            console.log('usunięto błąd na emailRepeat', controlToCompare.value);
            console.log(controlToCompare.hasError('notMatching'));
            return null;
        }

    } else if (control === control.parent?.get('emailRepeat')) {
        console.log('control to emailRepeat');
        controlToCompare = control.parent?.get('email');

        if (controlToCompare && controlToCompare.value !== control.value) {
            console.log('ustawiono błąd na emailRepeat', control.value);
            console.log(controlToCompare.hasError('notMatching'));

            return {notMatching: true};
        }
        else if (controlToCompare && controlToCompare.value === control.value) {
            console.log('usunięto błąd na emailRepeat', controlToCompare.value);
            console.log(controlToCompare.hasError('notMatching'));

            return null;

        }
    }
    return null;
}



