import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HostelComponent } from './hostel.component';


const routes: Routes = [
  { path: '', component: HostelComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HostelRoutingModule { }
