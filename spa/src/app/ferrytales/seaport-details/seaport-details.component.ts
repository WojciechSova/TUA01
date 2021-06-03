import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Seaport } from '../../model/mop/Seaport';

@Component({
    selector: 'app-seaport',
    templateUrl: './seaport-details.component.html',
    styleUrls: ['./seaport-details.component.less']
})
export class SeaportDetailsComponent implements OnInit {

    code = '';

    seaport: Seaport = {
        city: '',
        code: '',
        modificationDate: new Date(),
        creationDate: new Date(),
    };

    constructor(private route: ActivatedRoute) {
        this.code = this.route.snapshot.paramMap.get('code') as string;
    }

    ngOnInit(): void {
    }

}
