import { createReducer, on } from '@ngrx/store';

import * as AppActions from './app.actions';

export interface State {
}

const initialState: State = {
};

export const reducer = createReducer(
  initialState,
);
