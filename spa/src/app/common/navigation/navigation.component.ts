import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { IdentityService } from '../../services/utils/identity.service';

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.less']
})
export class NavigationComponent implements AfterViewInit {

    @Input()
    public isSticky = true;

    @Input()
    public loginVisible = false;

    @ViewChild('logo')
    private readonly logo: ElementRef;

    @ViewChild('navigation')
    private navigation: ElementRef;

    constructor(public identityService: IdentityService) {
        this.logo = new ElementRef('logo');
        this.navigation = new ElementRef('navigation');
    }

    ngAfterViewInit(): void {
        if (this.logo) {
            this.initObserver();
        }
    }

    initObserver(): void {
        const obsOptions: any = {
            root: null,
            threshold: 0
        };

        const observer = new IntersectionObserver(this.showNavbarOnScroll.bind(this), obsOptions);
        observer.observe(this.logo?.nativeElement);
    }

    showNavbarOnScroll(entries: any): void {
        if (!this.isSticky) {
            if (!entries[ 0 ].isIntersecting) {
                this.navigation?.nativeElement.classList.add('nav-sticky');
                this.navigation?.nativeElement.classList.remove('nav-non-sticky');
            } else {
                this.navigation?.nativeElement.classList.add('nav-non-sticky');
                this.navigation?.nativeElement.classList.remove('nav-sticky');
            }
        }
    }
}
