import { BaseEntity } from './../../shared';

export const enum RateCurrency {
    'CAD',
    'USD',
    'EUR'
}

export const enum RateUnit {
    'MINUTE',
    'HOUR',
    'DAY',
    'EVENT'
}

export class Rate implements BaseEntity {
    constructor(
        public id?: number,
        public description?: string,
        public amount?: number,
        public currency?: RateCurrency,
        public unit?: RateUnit,
        public userId?: number,
        public instructors?: BaseEntity[],
    ) {
    }
}
