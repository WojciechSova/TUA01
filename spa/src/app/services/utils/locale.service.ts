import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocaleService {

    private supportedLocales = ['pl', 'en-US', 'en'];

    public getLocale(): string {
        let locale = '';
        if (navigator.languages !== undefined) {
            locale = navigator.languages.filter(language => this.supportedLocales.includes(language))[0];
        } else {
            locale = navigator.language;
        }
        if (!locale) {
            return 'en-US';
        }
        return locale;
  }
}
