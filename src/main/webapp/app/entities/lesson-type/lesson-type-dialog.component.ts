import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LessonType } from './lesson-type.model';
import { LessonTypePopupService } from './lesson-type-popup.service';
import { LessonTypeService } from './lesson-type.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-lesson-type-dialog',
    templateUrl: './lesson-type-dialog.component.html'
})
export class LessonTypeDialogComponent implements OnInit {

    lessonType: LessonType;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private lessonTypeService: LessonTypeService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.lessonType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.lessonTypeService.update(this.lessonType));
        } else {
            this.subscribeToSaveResponse(
                this.lessonTypeService.create(this.lessonType));
        }
    }

    private subscribeToSaveResponse(result: Observable<LessonType>) {
        result.subscribe((res: LessonType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: LessonType) {
        this.eventManager.broadcast({ name: 'lessonTypeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-lesson-type-popup',
    template: ''
})
export class LessonTypePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lessonTypePopupService: LessonTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.lessonTypePopupService
                    .open(LessonTypeDialogComponent as Component, params['id']);
            } else {
                this.lessonTypePopupService
                    .open(LessonTypeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
