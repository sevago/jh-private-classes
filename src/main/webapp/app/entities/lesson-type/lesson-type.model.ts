import { BaseEntity } from './../../shared';

export class LessonType implements BaseEntity {
    constructor(
        public id?: number,
        public description?: string,
        public ratio?: number,
        public userId?: number,
    ) {
    }
}
