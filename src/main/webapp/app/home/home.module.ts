import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrivateclassesSharedModule } from '../shared';
import { CalendarModule } from 'angular-calendar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HOME_ROUTE, HomeComponent, CALENDAR_ROUTE } from './';
import { CalendarComponent } from '../calendar/calendar.component';

@NgModule({
    imports: [
        PrivateclassesSharedModule,
        BrowserAnimationsModule,
        CalendarModule.forRoot(),
        RouterModule.forRoot([ HOME_ROUTE, CALENDAR_ROUTE ], { useHash: true })
    ],
    declarations: [
        HomeComponent,
        CalendarComponent
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrivateclassesHomeModule {}
