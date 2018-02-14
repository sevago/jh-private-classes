import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Preferences } from './preferences.model';
import { PreferencesPopupService } from './preferences-popup.service';
import { PreferencesService } from './preferences.service';
import { User, UserService } from '../../shared';
import { Instructor, InstructorService } from '../instructor';
import { Activity, ActivityService } from '../activity';
import { Location, LocationService } from '../location';
import { LessonType, LessonTypeService } from '../lesson-type';
import { Rate, RateService } from '../rate';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-preferences-dialog',
    templateUrl: './preferences-dialog.component.html'
})
export class PreferencesDialogComponent implements OnInit {

    preferences: Preferences;
    isSaving: boolean;

    users: User[];

    instructors: Instructor[];

    activities: Activity[];

    locations: Location[];

    lessontypes: LessonType[];

    rates: Rate[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private preferencesService: PreferencesService,
        private userService: UserService,
        private instructorService: InstructorService,
        private activityService: ActivityService,
        private locationService: LocationService,
        private lessonTypeService: LessonTypeService,
        private rateService: RateService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.instructorService.query()
            .subscribe((res: ResponseWrapper) => { this.instructors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.activityService.query()
            .subscribe((res: ResponseWrapper) => { this.activities = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.locationService.query()
            .subscribe((res: ResponseWrapper) => { this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.lessonTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.lessontypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.rateService.query()
            .subscribe((res: ResponseWrapper) => { this.rates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.preferences.id !== undefined) {
            this.subscribeToSaveResponse(
                this.preferencesService.update(this.preferences));
        } else {
            this.subscribeToSaveResponse(
                this.preferencesService.create(this.preferences));
        }
    }

    private subscribeToSaveResponse(result: Observable<Preferences>) {
        result.subscribe((res: Preferences) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Preferences) {
        this.eventManager.broadcast({ name: 'preferencesListModification', content: 'OK'});
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

    trackInstructorById(index: number, item: Instructor) {
        return item.id;
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
}

@Component({
    selector: 'jhi-preferences-popup',
    template: ''
})
export class PreferencesPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private preferencesPopupService: PreferencesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.preferencesPopupService
                    .open(PreferencesDialogComponent as Component, params['id']);
            } else {
                this.preferencesPopupService
                    .open(PreferencesDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
