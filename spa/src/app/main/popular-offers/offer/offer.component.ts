import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CruiseGeneral } from '../../../model/mop/CruiseGeneral';
import { IdentityService } from '../../../services/utils/identity.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.less']
})
export class OfferComponent{
    @Input() imgPath: string | undefined;
    @Input()
    cruise: CruiseGeneral | undefined;

    @Output()
    loginVisible = new EventEmitter<boolean>();

  constructor(public identityService: IdentityService,
              private router: Router) { }

    changeLoginVisible(): void {
        this.loginVisible.emit(false);
        setTimeout(() => {this.loginVisible.emit(true); }, 10);
    }

    createBooking(cruiseNumber: any): void {
        this.router.navigate(['/ferrytales/booking/create/' + cruiseNumber as string]);
    }
}
