import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-class-info',
  templateUrl: './class-info.component.html',
  styleUrls: ['./class-info.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClassInfoComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
