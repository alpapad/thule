import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentRoutingModule } from './student-routing.module';
import { StudentComponent } from './student.component';
import { Card11Component } from './card11/card11.component';
import { Card111Component } from './card111/card111.component';
import { Card1111Component } from './card1111/card1111.component';
import { Card1112Component } from './card1112/card1112.component';
import { Card1Component } from './card1/card1.component';
import { Card12Component } from './card12/card12.component';
import { Card2Component } from './card2/card2.component';
import { Table1Component } from './table1/table1.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [StudentComponent, Card11Component, Card111Component, Card1111Component, Card1112Component, Card1Component, Card12Component, Card2Component, Table1Component],
  imports: [
    CommonModule,
    StudentRoutingModule,
    SharedModule
  ]
})
export class StudentModule { }
