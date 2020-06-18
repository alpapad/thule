import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LogInRoutingModule } from './log-in-routing.module';
import { LogInComponent } from './log-in.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [LogInComponent],
  imports: [
    CommonModule,
    LogInRoutingModule,
    SharedModule
  ]
})
export class LogInModule { }
