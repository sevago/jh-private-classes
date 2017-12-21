import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Instructor } from './instructor.model';
import { InstructorService } from './instructor.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import {ProfileService} from '../../layouts';

@Component({
    selector: 'jhi-instructor',
    templateUrl: './instructor.component.html'
})
export class InstructorComponent implements OnInit, OnDestroy {
instructors: Instructor[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    elasticsearchEnabled = false;

    constructor(
        private instructorService: InstructorService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private profileService: ProfileService
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.instructorService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.instructors = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.instructorService.query().subscribe(
            (res: ResponseWrapper) => {
                this.instructors = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInInstructors();
        this.getElasticsearchEnabled();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Instructor) {
        return item.id;
    }
    registerChangeInInstructors() {
        this.eventSubscriber = this.eventManager.subscribe('instructorListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    getElasticsearchEnabled() {
        this.profileService.getProfileInfo().then((profileInfo) => {
            this.elasticsearchEnabled = profileInfo.elasticsearchEnabled;
        });
    }
}
