import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaymentRoutingModule } from './payment-routing.module';
import { PaymentComponent } from './payment.component';
import { Card1511121Component } from './card1511121/card1511121.component';
import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [PaymentComponent, Card1511121Component],
  imports: [
    CommonModule,
    PaymentRoutingModule,
    SharedModule
  ]
})
export class PaymentModule { }
