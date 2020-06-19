import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HostelRoutingModule } from './hostel-routing.module';
import { HostelComponent } from './hostel.component';
import { Card1511Component } from './card1511/card1511.component';
import { Card22Component } from './card22/card22.component';
import { Table12Component } from './table12/table12.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [HostelComponent, Card1511Component, Card22Component, Table12Component],
  imports: [
    CommonModule,
    HostelRoutingModule,
    SharedModule
  ]
})
export class HostelModule { }
