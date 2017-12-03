import { BaseEntity } from './../../shared';

export const enum RateCurrency {
    'CAD',
    'USD',
    'EUR'
}

export class Invoice implements BaseEntity {
    constructor(
        public id?: number,
        public number?: number,
        public periodStartDate?: any,
        public periodEndDate?: any,
        public issueDate?: any,
        public dueDate?: any,
        public totalAmount?: number,
        public totalCurrency?: RateCurrency,
        public lessons?: BaseEntity[],
        public billToStudentId?: number,
        public teachingInstructorId?: number,
    ) {
    }
}
