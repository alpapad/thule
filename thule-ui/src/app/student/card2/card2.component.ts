import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-card2',
  templateUrl: './card2.component.html',
  styleUrls: ['./card2.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Card2Component implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
