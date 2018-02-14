import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PrivateclassesLessonModule } from './lesson/lesson.module';
import { PrivateclassesInstructorModule } from './instructor/instructor.module';
import { PrivateclassesLessonTypeModule } from './lesson-type/lesson-type.module';
import { PrivateclassesStudentModule } from './student/student.module';
import { PrivateclassesInvoiceModule } from './invoice/invoice.module';
import { PrivateclassesRateModule } from './rate/rate.module';
import { PrivateclassesActivityModule } from './activity/activity.module';
import { PrivateclassesLocationModule } from './location/location.module';
import { PrivateclassesPreferencesModule } from './preferences/preferences.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        PrivateclassesLessonModule,
        PrivateclassesInstructorModule,
        PrivateclassesLessonTypeModule,
        PrivateclassesStudentModule,
        PrivateclassesInvoiceModule,
        PrivateclassesRateModule,
        PrivateclassesActivityModule,
        PrivateclassesLocationModule,
        PrivateclassesPreferencesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrivateclassesEntityModule {}
