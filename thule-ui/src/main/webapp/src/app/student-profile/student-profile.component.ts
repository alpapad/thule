import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-student-profile',
  templateUrl: './student-profile.component.html',
  styleUrls: ['./student-profile.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StudentProfileComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
