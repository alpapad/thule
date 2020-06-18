import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-attendance',
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AttendanceComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
