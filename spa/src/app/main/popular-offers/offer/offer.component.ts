import { Component, Input } from '@angular/core';
import { CruiseGeneral } from '../../../model/mop/CruiseGeneral';

@Component({
  selector: 'app-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.less']
})
export class OfferComponent{
    @Input() imgPath: string | undefined;
    @Input()
    cruise: CruiseGeneral | undefined;

  constructor() { }

}
