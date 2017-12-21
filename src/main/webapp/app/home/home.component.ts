import {Component, OnDestroy, OnInit} from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import { Lesson } from '../entities/lesson/lesson.model';
import { LessonService } from '../entities/lesson/lesson.service';
import { ResponseWrapper } from '../shared/model/response-wrapper.model';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit, OnDestroy {
    account: Account;
    modalRef: NgbModalRef;
    lessons: Lesson[];
    eventSubscriber: Subscription;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService,
        private lessonService: LessonService
    ) {
    }

    ngOnInit() {
        if (this.isAuthenticated()) {
            this.loadAllLessons();
            this.principal.identity().then((account) => {
                this.account = account;
            });
        }
        this.registerAuthenticationSuccess();
        this.registerChangeInLessons();
        console.log('*** Instantiating Home Component ***');
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        console.log('*** Destroying Home Component ***');
    }

    registerChangeInLessons() {
        this.eventSubscriber = this.eventManager.subscribe(
            'lessonListModification',
            (response) => this.loadAllLessons());
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
            this.loadAllLessons();
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    loadAllLessons() {
        this.lessonService.query().subscribe(
            (res: ResponseWrapper) => {
                this.lessons = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
