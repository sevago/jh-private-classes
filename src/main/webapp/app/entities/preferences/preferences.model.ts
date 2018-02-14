import { BaseEntity } from './../../shared';

export class Preferences implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: number,
        public defaultInstructorId?: number,
        public defaultActivityId?: number,
        public defaultLocationId?: number,
        public defaultLessonTypeId?: number,
        public defaultRateId?: number,
    ) {
    }
}
