import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { LessonComponent } from './lesson.component';
import { LessonDetailComponent } from './lesson-detail.component';
import { LessonPopupComponent } from './lesson-dialog.component';
import { LessonDeletePopupComponent } from './lesson-delete-dialog.component';

@Injectable()
export class LessonResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const lessonRoute: Routes = [
    {
        path: 'lesson',
        component: LessonComponent,
        resolve: {
            'pagingParams': LessonResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lessons'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'lesson/:id',
        component: LessonDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lessons'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lessonPopupRoute: Routes = [
    {
        path: 'lesson-new',
        component: LessonPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lessons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'lesson/:id/edit',
        component: LessonPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lessons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'lesson/:id/delete',
        component: LessonDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lessons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
