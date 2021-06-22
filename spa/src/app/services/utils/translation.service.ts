import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class TranslationService {

    constructor() {
    }

    translateAccessLevel(accessLevelName: string | undefined): string {
        if (accessLevelName === undefined) {
            return '';
        }
        if (accessLevelName === 'ADMIN') {
            return 'general.administrator';
        } else if (accessLevelName === 'EMPLOYEE') {
            return 'general.employee';
        } else {
            return 'general.client';
        }
    }

    translateBoolean(value: boolean): string {
        if (value) {
            return 'general.true';
        } else {
            return 'general.false';
        }
    }
}
