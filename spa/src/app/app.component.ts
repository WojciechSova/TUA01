import { Component } from '@angular/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.less']
})
export class AppComponent {


    constructor() {
        setInterval(this.showNavbarOnScroll, 1000);
    }

    showNavbarOnScroll(): void
    {
        console.log('Cokolwiek');
        if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20){
            // @ts-ignore
            document.getElementById('navigation').style.top = '0';
        }
        else {
            // @ts-ignore
            document.getElementById('navigation').style.top = '-5vw';
        }
    }
}



