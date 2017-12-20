import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InstructorComponent } from './instructor.component';
import { InstructorDetailComponent } from './instructor-detail.component';
import { InstructorPopupComponent } from './instructor-dialog.component';
import { InstructorDeletePopupComponent } from './instructor-delete-dialog.component';
import { InstructorResolve } from './instructor.resolve';

export const instructorRoute: Routes = [
    {
        path: 'instructor',
        component: InstructorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Instructors'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'instructor/:id',
        component: InstructorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Instructors'
        },
        canActivate: [UserRouteAccessService],
        resolve: {
            instructor: InstructorResolve
        }
    }
];

export const instructorPopupRoute: Routes = [
    {
        path: 'instructor-new',
        component: InstructorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Instructors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'instructor/:id/edit',
        component: InstructorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Instructors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'instructor/:id/delete',
        component: InstructorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Instructors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
