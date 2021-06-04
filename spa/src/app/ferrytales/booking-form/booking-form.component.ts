import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.less']
})
export class BookingFormComponent implements OnInit {

    zoneA = false;
    zoneB = false;
    zoneC = false;
    zoneD = false;

  constructor() { }

  ngOnInit(): void {
  }

  pickZoneA(): void{
      this.zoneA = true;
      this.zoneB = false;
      this.zoneC = false;
      this.zoneD = false;
      console.log('A');
  }

  pickZoneB(): void{
        this.zoneA = false;
        this.zoneB = true;
        this.zoneC = false;
        this.zoneD = false;
        console.log('B');
  }

  pickZoneC(): void{
        this.zoneA = false;
        this.zoneB = false;
        this.zoneC = true;
        this.zoneD = false;
        console.log('C');
  }

  pickZoneD(): void{
        this.zoneA = false;
        this.zoneB = false;
        this.zoneC = false;
        this.zoneD = true;
        console.log('D');
  }
}
