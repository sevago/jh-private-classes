import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrivateclassesSharedModule } from '../shared';
import { CalendarModule } from 'angular-calendar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CalendarComponent } from '../calendar/calendar.component';
import { CALENDAR_ROUTE } from '../calendar/calendar.route';

@NgModule({
    imports: [
        PrivateclassesSharedModule,
        BrowserAnimationsModule,
        CalendarModule.forRoot(),
        RouterModule.forRoot([ CALENDAR_ROUTE ], { useHash: true })
    ],
    declarations: [
        CalendarComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrivateclassesCalendarModule {}
