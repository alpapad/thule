import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TeacherListRoutingModule } from './teacher-list-routing.module';
import { TeacherListComponent } from './teacher-list.component';
import { Card22112Component } from './card22112/card22112.component';
import { Table12112Component } from './table12112/table12112.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [TeacherListComponent, Card22112Component, Table12112Component],
  imports: [
    CommonModule,
    TeacherListRoutingModule,
    SharedModule
  ]
})
export class TeacherListModule { }
