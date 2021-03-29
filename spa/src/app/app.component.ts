import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.less']
})
export class AppComponent implements OnInit{


    @ViewChild('logo')
    private logo: ElementRef | undefined;
    @ViewChild('navigation')
    private navigation: ElementRef | undefined;

    private obsOptions: {
        root: null;
        threshold: 0;
    } | undefined;

    ngOnInit(): void {
        console.log(this.logo);
        const observer = new IntersectionObserver(this.showNavbarOnScroll, this.obsOptions);
        // @ts-ignore
        observer.observe(document.getElementById('logo'));
    }


    showNavbarOnScroll(entries: any): void {
        console.log('Cokolwiek');
        if (document.documentElement.scrollTop > 20) {
            // @ts-ignore
            document.getElementById('navigation').style.top = '0';
            console.log('if');
        }
        if (!entries.isIntersecting) {
            // @ts-ignore
            document.getElementById('navigation').style.top = '-5vw';
            console.log('else');
        }
    }
}



