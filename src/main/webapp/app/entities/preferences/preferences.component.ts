import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Preferences } from './preferences.model';
import { PreferencesService } from './preferences.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import {ProfileService} from '../../layouts';

@Component({
    selector: 'jhi-preferences',
    templateUrl: './preferences.component.html'
})
export class PreferencesComponent implements OnInit, OnDestroy {
preferences: Preferences[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    elasticsearchEnabled = false;
    isAdmin: boolean;

    constructor(
        private preferencesService: PreferencesService,
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
            this.preferencesService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.preferences = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.preferencesService.query().subscribe(
            (res: ResponseWrapper) => {
                this.preferences = res.json;
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
            this.isAdmin = account.authorities.indexOf('ROLE_ADMIN') !== -1;
        });
        this.registerChangeInPreferences();
        this.getElasticsearchEnabled();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Preferences) {
        return item.id;
    }
    registerChangeInPreferences() {
        this.eventSubscriber = this.eventManager.subscribe('preferencesListModification', (response) => this.loadAll());
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
