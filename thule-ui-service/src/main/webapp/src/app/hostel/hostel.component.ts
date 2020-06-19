import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-hostel',
  templateUrl: './hostel.component.html',
  styleUrls: ['./hostel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HostelComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
