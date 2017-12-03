import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrivateclassesSharedModule } from '../../shared';
import { PrivateclassesAdminModule } from '../../admin/admin.module';
import {
    RateService,
    RatePopupService,
    RateComponent,
    RateDetailComponent,
    RateDialogComponent,
    RatePopupComponent,
    RateDeletePopupComponent,
    RateDeleteDialogComponent,
    rateRoute,
    ratePopupRoute,
} from './';

const ENTITY_STATES = [
    ...rateRoute,
    ...ratePopupRoute,
];

@NgModule({
    imports: [
        PrivateclassesSharedModule,
        PrivateclassesAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RateComponent,
        RateDetailComponent,
        RateDialogComponent,
        RateDeleteDialogComponent,
        RatePopupComponent,
        RateDeletePopupComponent,
    ],
    entryComponents: [
        RateComponent,
        RateDialogComponent,
        RatePopupComponent,
        RateDeleteDialogComponent,
        RateDeletePopupComponent,
    ],
    providers: [
        RateService,
        RatePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrivateclassesRateModule {}
