import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-teacher-info',
  templateUrl: './teacher-info.component.html',
  styleUrls: ['./teacher-info.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TeacherInfoComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
