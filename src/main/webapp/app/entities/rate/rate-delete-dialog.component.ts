import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Rate } from './rate.model';
import { RatePopupService } from './rate-popup.service';
import { RateService } from './rate.service';

@Component({
    selector: 'jhi-rate-delete-dialog',
    templateUrl: './rate-delete-dialog.component.html'
})
export class RateDeleteDialogComponent {

    rate: Rate;

    constructor(
        private rateService: RateService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rateListModification',
                content: 'Deleted an rate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-rate-delete-popup',
    template: ''
})
export class RateDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ratePopupService: RatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.ratePopupService
                .open(RateDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
