import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SeaportGeneral } from '../../model/mop/SeaportGeneral';

@Component({
  selector: 'app-seaports-table',
  templateUrl: './seaports-table.component.html',
  styleUrls: ['./seaports-table.component.less']
})
export class SeaportsTableComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

    goToHomeBreadcrumb(): void {
        this.router.navigate(['/']);
    }

    listSeaports(): SeaportGeneral[] | null {
        return null;
    }

    getSeaports(): void {}
}
