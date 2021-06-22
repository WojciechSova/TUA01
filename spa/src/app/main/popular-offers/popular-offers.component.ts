import { Component } from '@angular/core';
import { IdentityService } from '../../services/utils/identity.service';
import { CruiseGeneralService } from '../../services/mop/cruise-general.service';
import { CruiseGeneral } from '../../model/mop/CruiseGeneral';

@Component({
  selector: 'app-popular-offers',
  templateUrl: './popular-offers.component.html',
  styleUrls: ['./popular-offers.component.less']
})
export class PopularOffersComponent {

  constructor(public identityService: IdentityService,
              private cruiseGeneralService: CruiseGeneralService) {
      this.getCurrentCruises();
  }

    getCurrentCruises(): void {
        this.cruiseGeneralService.getCurrentCruises().subscribe(
            (response: CruiseGeneral[]) => {
                this.cruiseGeneralService.readCurrentCruises(response);
            });
    }

    getRouteByCruise(num: number): CruiseGeneral {
        return this.cruiseGeneralService.currentCruises[num];
    }

}
