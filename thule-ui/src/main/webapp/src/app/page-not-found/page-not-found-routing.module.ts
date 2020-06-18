import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PageNotFoundComponent } from './page-not-found.component';


const routes: Routes = [
  { path: '', component: PageNotFoundComponent, data: { layout: {
        paddings: {
          paddingTop: 0,
          paddingRight: 0,
          paddingBottom: 0,
          paddingLeft: 0,
          paddingTopUnit: "px",
          paddingRightUnit: "px",
          paddingBottomUnit: "px",
          paddingLeftUnit: "px"
        },
        header: true,
        sidebar: false
      }
    }
 },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PageNotFoundRoutingModule { }
