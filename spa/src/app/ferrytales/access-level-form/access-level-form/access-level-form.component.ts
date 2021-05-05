import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
    selector: 'app-access-level-form',
    templateUrl: './access-level-form.component.html',
    styleUrls: ['./access-level-form.component.less']
})
export class AccessLevelFormComponent implements OnInit {

    constructor() {
    }

    @Output()
    isAccessLevelFormVisibleChange = new EventEmitter<boolean>();

    ngOnInit(): void {
    }

    closeComponent(): void {
        this.isAccessLevelFormVisibleChange.emit(false);
    }
}
