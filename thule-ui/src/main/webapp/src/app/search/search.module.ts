import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SearchRoutingModule } from './search-routing.module';
import { SearchComponent } from './search.component';
import { Card15111Component } from './card15111/card15111.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [SearchComponent, Card15111Component],
  imports: [
    CommonModule,
    SearchRoutingModule,
    SharedModule
  ]
})
export class SearchModule { }
