import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrivateclassesSharedModule } from '../../shared';
import { PrivateclassesAdminModule } from '../../admin/admin.module';
import {
    StudentResolve,
    StudentService,
    StudentPopupService,
    StudentComponent,
    StudentDetailComponent,
    StudentDialogComponent,
    StudentPopupComponent,
    StudentDeletePopupComponent,
    StudentDeleteDialogComponent,
    studentRoute,
    studentPopupRoute,
    StudentResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...studentRoute,
    ...studentPopupRoute,
];

@NgModule({
    imports: [
        PrivateclassesSharedModule,
        PrivateclassesAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        StudentComponent,
        StudentDetailComponent,
        StudentDialogComponent,
        StudentDeleteDialogComponent,
        StudentPopupComponent,
        StudentDeletePopupComponent,
    ],
    entryComponents: [
        StudentComponent,
        StudentDialogComponent,
        StudentPopupComponent,
        StudentDeleteDialogComponent,
        StudentDeletePopupComponent,
    ],
    providers: [
        StudentService,
        StudentPopupService,
        StudentResolvePagingParams,
        StudentResolve,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrivateclassesStudentModule {}
