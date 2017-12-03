import { BaseEntity } from './../../shared';

export class Instructor implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public address?: string,
        public phoneNumber?: string,
        public email?: string,
        public invoices?: BaseEntity[],
        public lessons?: BaseEntity[],
        public userId?: number,
        public rates?: BaseEntity[],
    ) {
    }
}
