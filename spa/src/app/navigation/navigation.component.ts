import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.less']
})
export class NavigationComponent implements AfterViewInit {

    @Input()
    public isSticky = true;

    public stickyStyles = true;

    @ViewChild('logo')
    private logo: ElementRef;

    @ViewChild('navigation')
    private navigation: ElementRef;

    constructor() {
        this.logo = new ElementRef('logo');
        this.navigation = new ElementRef('navigation');
    }

    ngAfterViewInit(): void {
        const obsOptions: any = {
            root: null,
            threshold: 0
        };

        const observer = new IntersectionObserver(this.showNavbarOnScroll.bind(this), obsOptions);
        observer.observe(this.logo?.nativeElement);
    }

    showNavbarOnScroll(entries: any): void {
        if (!this.isSticky) {
            if (!entries[0].isIntersecting) {
                this.navigation?.nativeElement.classList.add('nav-sticky');
                this.navigation?.nativeElement.classList.remove('nav-non-sticky');
                this.stickyStyles = true;
            } else {
                this.navigation?.nativeElement.classList.add('nav-non-sticky');
                this.navigation?.nativeElement.classList.remove('nav-sticky');
                this.stickyStyles = false;
            }
        }
    }
}
