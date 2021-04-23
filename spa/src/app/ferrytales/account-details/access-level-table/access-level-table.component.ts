import {Component, Input, OnInit} from '@angular/core';
import {AccessLevel} from '../../../model/mok/AccessLevel';

@Component({
    selector: 'app-access-level-table',
    templateUrl: './access-level-table.component.html',
    styleUrls: ['./access-level-table.component.less']
})
export class AccessLevelTableComponent implements OnInit {

    constructor() {
    }

    @Input()
    accessLevels: AccessLevel[] = [];

    ngOnInit(): void {
    }
}
