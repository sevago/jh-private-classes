import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Rate } from './rate.model';
import { RateService } from './rate.service';

@Component({
    selector: 'jhi-rate-detail',
    templateUrl: './rate-detail.component.html'
})
export class RateDetailComponent implements OnInit, OnDestroy {

    rate: Rate;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private rateService: RateService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.data.subscribe((data: {rate: Rate}) => {
            this.rate = data.rate;
        });
        this.registerChangeInRates();
    }

    load(id) {
        this.rateService.find(id).subscribe((rate) => {
            this.rate = rate;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'rateListModification',
            (response) => this.load(this.rate.id)
        );
    }
}
