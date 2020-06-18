import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-card1',
  templateUrl: './card1.component.html',
  styleUrls: ['./card1.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Card1Component implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
