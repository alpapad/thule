import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentDetailsRoutingModule } from './student-details-routing.module';
import { StudentDetailsComponent } from './student-details.component';
import { Card141Component } from './card141/card141.component';
import { Card131Component } from './card131/card131.component';
import { Card1211Component } from './card1211/card1211.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [StudentDetailsComponent, Card141Component, Card131Component, Card1211Component],
  imports: [
    CommonModule,
    StudentDetailsRoutingModule,
    SharedModule
  ]
})
export class StudentDetailsModule { }
