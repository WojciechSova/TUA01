import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-offer',
  templateUrl: './offer.component.html',
  styleUrls: ['./offer.component.less']
})
export class OfferComponent implements OnInit {
    @Input() imgPath: string | undefined;

  constructor() { }

  ngOnInit(): void {
  }

}
