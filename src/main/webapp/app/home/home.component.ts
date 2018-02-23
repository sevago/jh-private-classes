import {Component, OnDestroy, OnInit} from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import { ResponseWrapper } from '../shared/model/response-wrapper.model';
import { Subscription } from 'rxjs/Rx';
import { Lesson, LessonService } from '../entities/lesson';
import { Invoice, InvoiceService} from '../entities/invoice';
import { Preferences, PreferencesService } from '../entities/preferences';

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
    invoices: Invoice[];
    preferences: Preferences
    eventSubscriber: Subscription;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private jhiAlertService: JhiAlertService,
        private lessonService: LessonService,
        private invoiceService: InvoiceService,
        private preferncesService: PreferencesService
    ) {
    }

    ngOnInit() {
        if (this.isAuthenticated()) {
            this.loadAllData();
            this.principal.identity().then((account) => {
                this.account = account;
            });
        }
        this.registerAuthenticationSuccess();
        this.registerForChanges();
        console.log('*** Instantiating Home Component ***');
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        console.log('*** Destroying Home Component ***');
    }

    registerForChanges() {
        this.eventSubscriber = this.eventManager.subscribe(
            'lessonListModification',
            (response) => this.loadAllData());
        this.eventSubscriber = this.eventManager.subscribe(
            'invoiceListModification',
            (response) => this.loadAllData());
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
            this.loadAllData();
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    loadAllData() {
        this.lessonService.query().subscribe(
            (res: ResponseWrapper) => {
                this.lessons = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
        this.invoiceService.query().subscribe(
            (res: ResponseWrapper) => {
                this.invoices = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
        this.preferncesService.user().subscribe((preferences: Preferences) => {
            this.preferences = preferences;
        });
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
