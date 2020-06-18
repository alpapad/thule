import { createAction, props } from '@ngrx/store';

export const toggleSidebar = createAction('[App] Toggle Sidebar', props<{ data: any }>());
