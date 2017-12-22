import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AgmCoreModule } from '@agm/core';

import { PrivateclassesSharedModule } from '../../shared';
import { PrivateclassesAdminModule } from '../../admin/admin.module';
import {
    LocationResolve,
    LocationService,
    LocationPopupService,
    LocationComponent,
    LocationDetailComponent,
    LocationDialogComponent,
    LocationPopupComponent,
    LocationDeletePopupComponent,
    LocationDeleteDialogComponent,
    locationRoute,
    locationPopupRoute,
} from './';
import { GMapsService } from '../../services/google-maps.service';

const ENTITY_STATES = [
    ...locationRoute,
    ...locationPopupRoute,
];

@NgModule({
    imports: [
        PrivateclassesSharedModule,
        PrivateclassesAdminModule,
        RouterModule.forChild(ENTITY_STATES),
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyDZ4jTUBY_rymf53VvjZRVGWD0hcG3bHik'
        })
    ],
    declarations: [
        LocationComponent,
        LocationDetailComponent,
        LocationDialogComponent,
        LocationDeleteDialogComponent,
        LocationPopupComponent,
        LocationDeletePopupComponent,
    ],
    entryComponents: [
        LocationComponent,
        LocationDialogComponent,
        LocationPopupComponent,
        LocationDeleteDialogComponent,
        LocationDeletePopupComponent,
    ],
    providers: [
        LocationService,
        LocationPopupService,
        LocationResolve,
        GMapsService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrivateclassesLocationModule {}
