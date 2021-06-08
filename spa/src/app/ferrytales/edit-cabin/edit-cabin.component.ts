import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {IdentityService} from '../../services/utils/identity.service';
import {CabinDetailsService} from "../../services/mop/cabin-details.service";

@Component({
  selector: 'app-edit-cabin',
  templateUrl: './edit-cabin.component.html',
  styleUrls: ['./edit-cabin.component.less']
})
export class EditCabinComponent implements OnInit {

  constructor(private router: Router,
              public identityService: IdentityService,
              public cabinDetailsService: CabinDetailsService) { }

  ngOnInit(): void {
  }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    goToFerriesListBreadcrumb(): void {

    }

    goToFerryBreadcrumb(): void {

    }

    goToCabinBreadcrumb(): void {

    }


}
