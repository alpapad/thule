import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentProfileRoutingModule } from './student-profile-routing.module';
import { StudentProfileComponent } from './student-profile.component';
import { Card112Component } from './card112/card112.component';
import { Card1121Component } from './card1121/card1121.component';
import { Card1122Component } from './card1122/card1122.component';
import { Card1123Component } from './card1123/card1123.component';
import { Card13Component } from './card13/card13.component';
import { Card211Component } from './card211/card211.component';
import { Card212Component } from './card212/card212.component';
import { Table111Component } from './table111/table111.component';
import { Card2111Component } from './card2111/card2111.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [StudentProfileComponent, Card112Component, Card1121Component, Card1122Component, Card1123Component, Card13Component, Card211Component, Card212Component, Table111Component, Card2111Component],
  imports: [
    CommonModule,
    StudentProfileRoutingModule,
    SharedModule
  ]
})
export class StudentProfileModule { }
