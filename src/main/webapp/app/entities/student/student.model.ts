import { BaseEntity } from './../../shared';

export class Student implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public phoneNumber?: string,
        public email?: string,
        public invoices?: BaseEntity[],
        public userId?: number,
        public lessons?: BaseEntity[],
    ) {
    }
}
