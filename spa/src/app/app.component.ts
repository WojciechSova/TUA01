import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.less']
})
export class AppComponent implements AfterViewInit{


    @ViewChild('logo')
    private logo: ElementRef | undefined;
    @ViewChild('navigation')
    private navigation: ElementRef | undefined;


    ngAfterViewInit(): void {
        const obsOptions: any = {
            root: null,
            threshold: 0
        };

        const observer = new IntersectionObserver(this.showNavbarOnScroll.bind(this), obsOptions);
        observer.observe(this.logo?.nativeElement);
    }

    showNavbarOnScroll(entries: any): void {
        if (!entries[0].isIntersecting) {
            this.navigation?.nativeElement.classList.add('nav-sticky');
            this.navigation?.nativeElement.classList.remove('nav-non-sticky');
        } else {
            this.navigation?.nativeElement.classList.add('nav-non-sticky');
            this.navigation?.nativeElement.classList.remove('nav-sticky');
        }
    }
}




