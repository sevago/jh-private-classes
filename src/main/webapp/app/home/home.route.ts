import { Route } from '@angular/router';

import { HomeComponent } from './';
import { CalendarComponent } from '../calendar/calendar.component';

export const HOME_ROUTE: Route = {
    path: '',
    component: HomeComponent,
    data: {
        authorities: [],
        pageTitle: 'My Private Classes Application'
    }
};

export const CALENDAR_ROUTE: Route = {
    path: 'calendar',
    component: CalendarComponent,
    data: {
        authorities: [],
        pageTitle: 'My Calendar'
    }
};
