import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TeacherInfoComponent } from './teacher-info.component';


const routes: Routes = [
  { path: '', component: TeacherInfoComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TeacherInfoRoutingModule { }
