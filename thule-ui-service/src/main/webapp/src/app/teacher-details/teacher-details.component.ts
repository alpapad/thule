import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-teacher-details',
  templateUrl: './teacher-details.component.html',
  styleUrls: ['./teacher-details.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TeacherDetailsComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
