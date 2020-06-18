import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TeacherDetailsRoutingModule } from './teacher-details-routing.module';
import { TeacherDetailsComponent } from './teacher-details.component';
import { Card1411Component } from './card1411/card1411.component';
import { Card15Component } from './card15/card15.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [TeacherDetailsComponent, Card1411Component, Card15Component],
  imports: [
    CommonModule,
    TeacherDetailsRoutingModule,
    SharedModule
  ]
})
export class TeacherDetailsModule { }
