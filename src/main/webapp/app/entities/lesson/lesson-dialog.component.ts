import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Lesson } from './lesson.model';
import { LessonPopupService } from './lesson-popup.service';
import { LessonService } from './lesson.service';
import { Activity, ActivityService } from '../activity';
import { Location, LocationService } from '../location';
import { LessonType, LessonTypeService } from '../lesson-type';
import { Rate, RateService } from '../rate';
import { Student, StudentService } from '../student';
import { Instructor, InstructorService } from '../instructor';
import { Invoice, InvoiceService } from '../invoice';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-lesson-dialog',
    templateUrl: './lesson-dialog.component.html'
})
export class LessonDialogComponent implements OnInit {

    lesson: Lesson;
    isSaving: boolean;

    activities: Activity[];

    locations: Location[];

    lessontypes: LessonType[];

    rates: Rate[];

    students: Student[];

    instructors: Instructor[];

    invoices: Invoice[];
    dateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private lessonService: LessonService,
        private activityService: ActivityService,
        private locationService: LocationService,
        private lessonTypeService: LessonTypeService,
        private rateService: RateService,
        private studentService: StudentService,
        private instructorService: InstructorService,
        private invoiceService: InvoiceService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.activityService.query()
            .subscribe((res: ResponseWrapper) => { this.activities = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.locationService.query()
            .subscribe((res: ResponseWrapper) => { this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.lessonTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.lessontypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.rateService.query()
            .subscribe((res: ResponseWrapper) => { this.rates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.studentService.query()
            .subscribe((res: ResponseWrapper) => { this.students = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.instructorService.query()
            .subscribe((res: ResponseWrapper) => { this.instructors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.invoiceService.query()
            .subscribe((res: ResponseWrapper) => { this.invoices = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.lesson.id !== undefined) {
            this.subscribeToSaveResponse(
                this.lessonService.update(this.lesson));
        } else {
            this.subscribeToSaveResponse(
                this.lessonService.create(this.lesson));
        }
    }

    private subscribeToSaveResponse(result: Observable<Lesson>) {
        result.subscribe((res: Lesson) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Lesson) {
        this.eventManager.broadcast({ name: 'lessonListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackActivityById(index: number, item: Activity) {
        return item.id;
    }

    trackLocationById(index: number, item: Location) {
        return item.id;
    }

    trackLessonTypeById(index: number, item: LessonType) {
        return item.id;
    }

    trackRateById(index: number, item: Rate) {
        return item.id;
    }

    trackStudentById(index: number, item: Student) {
        return item.id;
    }

    trackInstructorById(index: number, item: Instructor) {
        return item.id;
    }

    trackInvoiceById(index: number, item: Invoice) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-lesson-popup',
    template: ''
})
export class LessonPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lessonPopupService: LessonPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.lessonPopupService
                    .open(LessonDialogComponent as Component, params['id']);
            } else {
                this.lessonPopupService
                    .open(LessonDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
