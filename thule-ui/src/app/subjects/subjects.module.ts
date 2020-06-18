import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SubjectsRoutingModule } from './subjects-routing.module';
import { SubjectsComponent } from './subjects.component';
import { Card15112Component } from './card15112/card15112.component';
import { Card222Component } from './card222/card222.component';
import { Table122Component } from './table122/table122.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [SubjectsComponent, Card15112Component, Card222Component, Table122Component],
  imports: [
    CommonModule,
    SubjectsRoutingModule,
    SharedModule
  ]
})
export class SubjectsModule { }
