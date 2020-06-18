import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import * as AppActions from './app.actions';
import { tap, map } from 'rxjs/operators';
import { NbSidebarService } from '@nebular/theme';


@Injectable()
export class AppEffects {

  private toggleSidebar$ = createEffect(() => this.actions$.pipe(
    ofType(AppActions.toggleSidebar.type),
    map((result: { data: any }) => result.data),
    tap(() => this.sidebarService.toggle()),
  ), { dispatch: false });

  constructor(private sidebarService: NbSidebarService,
              private actions$: Actions) {
  }

}
