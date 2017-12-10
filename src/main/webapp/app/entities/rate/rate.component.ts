import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Rate } from './rate.model';
import { RateService } from './rate.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import {ProfileService} from '../../layouts';

@Component({
    selector: 'jhi-rate',
    templateUrl: './rate.component.html'
})
export class RateComponent implements OnInit, OnDestroy {
rates: Rate[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    elasticsearchEnabled: boolean;

    constructor(
        private rateService: RateService,
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
            this.rateService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.rates = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.rateService.query().subscribe(
            (res: ResponseWrapper) => {
                this.rates = res.json;
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
        this.registerChangeInRates();
        this.getElasticsearchEnabled();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Rate) {
        return item.id;
    }
    registerChangeInRates() {
        this.eventSubscriber = this.eventManager.subscribe('rateListModification', (response) => this.loadAll());
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
