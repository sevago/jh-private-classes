import { CalendarComponent } from './calendar.component';
import { Route } from '@angular/router';
import { UserRouteAccessService } from '../shared';

export const CALENDAR_ROUTE: Route = {
    path: 'calendar',
    component: CalendarComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Calendar'
    },
    canActivate: [UserRouteAccessService]
};
