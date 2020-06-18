import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClassInfoRoutingModule } from './class-info-routing.module';
import { ClassInfoComponent } from './class-info.component';
import { Card15111111Component } from './card15111111/card15111111.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [ClassInfoComponent, Card15111111Component],
  imports: [
    CommonModule,
    ClassInfoRoutingModule,
    SharedModule
  ]
})
export class ClassInfoModule { }
