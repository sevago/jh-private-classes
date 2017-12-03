import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LessonType } from './lesson-type.model';
import { LessonTypePopupService } from './lesson-type-popup.service';
import { LessonTypeService } from './lesson-type.service';

@Component({
    selector: 'jhi-lesson-type-delete-dialog',
    templateUrl: './lesson-type-delete-dialog.component.html'
})
export class LessonTypeDeleteDialogComponent {

    lessonType: LessonType;

    constructor(
        private lessonTypeService: LessonTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.lessonTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'lessonTypeListModification',
                content: 'Deleted an lessonType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-lesson-type-delete-popup',
    template: ''
})
export class LessonTypeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lessonTypePopupService: LessonTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.lessonTypePopupService
                .open(LessonTypeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
