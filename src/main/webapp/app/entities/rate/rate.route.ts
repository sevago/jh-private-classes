import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RateComponent } from './rate.component';
import { RateDetailComponent } from './rate-detail.component';
import { RatePopupComponent } from './rate-dialog.component';
import { RateDeletePopupComponent } from './rate-delete-dialog.component';

export const rateRoute: Routes = [
    {
        path: 'rate',
        component: RateComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rates'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'rate/:id',
        component: RateDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rates'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ratePopupRoute: Routes = [
    {
        path: 'rate-new',
        component: RatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rate/:id/edit',
        component: RatePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'rate/:id/delete',
        component: RateDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Rates'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
