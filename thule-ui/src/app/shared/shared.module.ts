import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NbTooltipModule, NbCardModule, NbIconModule, NbInputModule, NbCheckboxModule, NbSelectModule, NbButtonModule, NbDatepickerModule, NbFormFieldModule } from '@nebular/theme';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    NbTooltipModule,
    NbCardModule,
    NbIconModule,
    NbInputModule,
    NbCheckboxModule,
    NbSelectModule,
    NbButtonModule,
    NbDatepickerModule,
    NbFormFieldModule
  ],
  exports: [NbTooltipModule, NbCardModule, NbIconModule, NbInputModule, NbCheckboxModule, NbSelectModule, NbButtonModule, NbDatepickerModule, NbFormFieldModule]
})
export class SharedModule { }
