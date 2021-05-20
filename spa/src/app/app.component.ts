import { Component } from '@angular/core';
import { TranslateService } from "@ngx-translate/core";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.less']
})
export class AppComponent {
    constructor(public translateService: TranslateService) {
        translateService.addLangs(['en', 'pl'])
        translateService.setDefaultLang('en')
        const browserLang = translateService.getBrowserLang();
        translateService.use(browserLang.match(/en|pl/) ? browserLang: 'en')
    }
}
