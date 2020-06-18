import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ClassInfoComponent } from './class-info.component';


const routes: Routes = [
  { path: '', component: ClassInfoComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClassInfoRoutingModule { }
