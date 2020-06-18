import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AttendanceRoutingModule } from './attendance-routing.module';
import { AttendanceComponent } from './attendance.component';
import { Card151112Component } from './card151112/card151112.component';
import { Card22111Component } from './card22111/card22111.component';
import { Table12111Component } from './table12111/table12111.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [AttendanceComponent, Card151112Component, Card22111Component, Table12111Component],
  imports: [
    CommonModule,
    AttendanceRoutingModule,
    SharedModule
  ]
})
export class AttendanceModule { }
