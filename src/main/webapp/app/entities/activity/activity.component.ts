import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Activity } from './activity.model';
import { ActivityService } from './activity.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import {ProfileService} from '../../layouts';

@Component({
    selector: 'jhi-activity',
    templateUrl: './activity.component.html'
})
export class ActivityComponent implements OnInit, OnDestroy {
activities: Activity[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    elasticsearchEnabled = false;

    constructor(
        private activityService: ActivityService,
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
            this.activityService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.activities = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.activityService.query().subscribe(
            (res: ResponseWrapper) => {
                this.activities = res.json;
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
        this.registerChangeInActivities();
        this.getElasticsearchEnabled();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Activity) {
        return item.id;
    }
    registerChangeInActivities() {
        this.eventSubscriber = this.eventManager.subscribe('activityListModification', (response) => this.loadAll());
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
