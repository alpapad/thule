import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentListRoutingModule } from './student-list-routing.module';
import { StudentListComponent } from './student-list.component';
import { Card221Component } from './card221/card221.component';
import { Table121Component } from './table121/table121.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [StudentListComponent, Card221Component, Table121Component],
  imports: [
    CommonModule,
    StudentListRoutingModule,
    SharedModule
  ]
})
export class StudentListModule { }
