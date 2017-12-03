import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrivateclassesSharedModule } from '../../shared';
import { PrivateclassesAdminModule } from '../../admin/admin.module';
import {
    InstructorService,
    InstructorPopupService,
    InstructorComponent,
    InstructorDetailComponent,
    InstructorDialogComponent,
    InstructorPopupComponent,
    InstructorDeletePopupComponent,
    InstructorDeleteDialogComponent,
    instructorRoute,
    instructorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...instructorRoute,
    ...instructorPopupRoute,
];

@NgModule({
    imports: [
        PrivateclassesSharedModule,
        PrivateclassesAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        InstructorComponent,
        InstructorDetailComponent,
        InstructorDialogComponent,
        InstructorDeleteDialogComponent,
        InstructorPopupComponent,
        InstructorDeletePopupComponent,
    ],
    entryComponents: [
        InstructorComponent,
        InstructorDialogComponent,
        InstructorPopupComponent,
        InstructorDeleteDialogComponent,
        InstructorDeletePopupComponent,
    ],
    providers: [
        InstructorService,
        InstructorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrivateclassesInstructorModule {}
