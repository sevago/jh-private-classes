import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { LessonType } from './lesson-type.model';
import { LessonTypeService } from './lesson-type.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import {ProfileService} from '../../layouts';

@Component({
    selector: 'jhi-lesson-type',
    templateUrl: './lesson-type.component.html'
})
export class LessonTypeComponent implements OnInit, OnDestroy {
lessonTypes: LessonType[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    elasticsearchEnabled: boolean;

    constructor(
        private lessonTypeService: LessonTypeService,
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
            this.lessonTypeService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.lessonTypes = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.lessonTypeService.query().subscribe(
            (res: ResponseWrapper) => {
                this.lessonTypes = res.json;
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
        this.registerChangeInLessonTypes();
        this.getElasticsearchEnabled();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: LessonType) {
        return item.id;
    }
    registerChangeInLessonTypes() {
        this.eventSubscriber = this.eventManager.subscribe('lessonTypeListModification', (response) => this.loadAll());
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
