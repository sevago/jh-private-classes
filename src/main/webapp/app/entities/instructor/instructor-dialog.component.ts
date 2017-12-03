import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Instructor } from './instructor.model';
import { InstructorPopupService } from './instructor-popup.service';
import { InstructorService } from './instructor.service';
import { User, UserService } from '../../shared';
import { Rate, RateService } from '../rate';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-instructor-dialog',
    templateUrl: './instructor-dialog.component.html'
})
export class InstructorDialogComponent implements OnInit {

    instructor: Instructor;
    isSaving: boolean;

    users: User[];

    rates: Rate[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private instructorService: InstructorService,
        private userService: UserService,
        private rateService: RateService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.rateService.query()
            .subscribe((res: ResponseWrapper) => { this.rates = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.instructor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.instructorService.update(this.instructor));
        } else {
            this.subscribeToSaveResponse(
                this.instructorService.create(this.instructor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Instructor>) {
        result.subscribe((res: Instructor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Instructor) {
        this.eventManager.broadcast({ name: 'instructorListModification', content: 'OK'});
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

    trackRateById(index: number, item: Rate) {
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
    selector: 'jhi-instructor-popup',
    template: ''
})
export class InstructorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private instructorPopupService: InstructorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.instructorPopupService
                    .open(InstructorDialogComponent as Component, params['id']);
            } else {
                this.instructorPopupService
                    .open(InstructorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
