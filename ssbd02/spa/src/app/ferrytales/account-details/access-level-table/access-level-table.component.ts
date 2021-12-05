import { Component, Input, OnInit } from '@angular/core';
import { AccessLevel } from '../../../model/mok/AccessLevel';
import {IdentityService} from '../../../services/utils/identity.service';
import { TranslationService } from '../../../services/utils/translation.service';

@Component({
    selector: 'app-access-level-table',
    templateUrl: './access-level-table.component.html',
    styleUrls: ['./access-level-table.component.less']
})
export class AccessLevelTableComponent implements OnInit {


    constructor(public identityService: IdentityService,
                public translationService: TranslationService) {
    }

    @Input()
    accessLevels: AccessLevel[] = [];

    ngOnInit(): void {
    }
}
