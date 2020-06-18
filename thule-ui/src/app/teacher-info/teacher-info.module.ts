import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TeacherInfoRoutingModule } from './teacher-info-routing.module';
import { TeacherInfoComponent } from './teacher-info.component';
import { Card151111Component } from './card151111/card151111.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [TeacherInfoComponent, Card151111Component],
  imports: [
    CommonModule,
    TeacherInfoRoutingModule,
    SharedModule
  ]
})
export class TeacherInfoModule { }
