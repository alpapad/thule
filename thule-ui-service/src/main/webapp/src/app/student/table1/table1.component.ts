import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-table1',
  templateUrl: './table1.component.html',
  styleUrls: ['./table1.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Table1Component implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
