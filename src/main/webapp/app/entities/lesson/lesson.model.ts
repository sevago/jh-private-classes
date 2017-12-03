import { BaseEntity } from './../../shared';

export class Lesson implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public duration?: number,
        public totalCharge?: number,
        public activityId?: number,
        public locationId?: number,
        public lessonTypeId?: number,
        public rateId?: number,
        public students?: BaseEntity[],
        public teachingInstructorId?: number,
        public invoices?: BaseEntity[],
    ) {
    }
}
