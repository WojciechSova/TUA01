import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-ferrytales',
    templateUrl: './ferrytales.component.html',
    styleUrls: ['./ferrytales.component.less']
})
export class FerrytalesComponent implements OnInit {

    constructor(private router: Router) {
    }

    ngOnInit(): void {
    }

    goToHomePage(): void {
        console.log('HELLO');
        this.router.navigate(['/']);
    }

}
