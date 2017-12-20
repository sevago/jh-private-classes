import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrivateclassesSharedModule } from '../../shared';
import {
    LessonResolve,
    LessonService,
    LessonPopupService,
    LessonComponent,
    LessonDetailComponent,
    LessonDialogComponent,
    LessonPopupComponent,
    LessonDeletePopupComponent,
    LessonDeleteDialogComponent,
    lessonRoute,
    lessonPopupRoute,
    LessonResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...lessonRoute,
    ...lessonPopupRoute,
];

@NgModule({
    imports: [
        PrivateclassesSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        LessonComponent,
        LessonDetailComponent,
        LessonDialogComponent,
        LessonDeleteDialogComponent,
        LessonPopupComponent,
        LessonDeletePopupComponent,
    ],
    entryComponents: [
        LessonComponent,
        LessonDialogComponent,
        LessonPopupComponent,
        LessonDeleteDialogComponent,
        LessonDeletePopupComponent,
    ],
    providers: [
        LessonService,
        LessonPopupService,
        LessonResolvePagingParams,
        LessonResolve,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrivateclassesLessonModule {}
