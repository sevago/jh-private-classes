import { Component, OnInit, OnDestroy, NgZone } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import {Observable, Subscription} from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Location } from './location.model';
import { LocationPopupService } from './location-popup.service';
import { LocationService } from './location.service';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';
import { GMapsService } from '../../services/google-maps.service';
import { FormControl } from '@angular/forms';

@Component({
    selector: 'jhi-location-dialog',
    templateUrl: './location-dialog.component.html'
})
export class LocationDialogComponent implements OnInit, OnDestroy {
    location: Location;
    isSaving: boolean;
    latitude: number;
    longitude: number;
    locationChosen = false;
    geocodingError: any;
    address = new FormControl();
    valueChangesSubscription: Subscription;

    subscriber: any;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private locationService: LocationService,
        private userService: UserService,
        private eventManager: JhiEventManager,
        private gMapsService: GMapsService,
        private __zone: NgZone
    ) {
    }

    ngOnInit() {
        this.valueChangesSubscription = this.onChanges();
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    ngOnDestroy(): void {
        this.valueChangesSubscription.unsubscribe();
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.location.id !== undefined) {
            this.subscribeToSaveResponse(
                this.locationService.update(this.location));
        } else {
            this.subscribeToSaveResponse(
                this.locationService.create(this.location));
        }
    }

    private subscribeToSaveResponse(result: Observable<Location>) {
        result.subscribe((res: Location) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Location) {
        this.eventManager.broadcast({ name: 'locationListModification', content: 'OK'});
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

    onChanges() {
        return this.address.valueChanges
            .debounceTime(500)
            .distinctUntilChanged()
            .filter((address) => address)
            .switchMap((address) => this.gMapsService.getLatLan(address))
            .subscribe(
                (result) => {
                    this.__zone.run(() => {
                        this.latitude = result.lat();
                        this.longitude = result.lng();
                        this.geocodingError = undefined;
                        this.locationChosen = true;
                    })
                },
                (error) => {
                    this.geocodingError = 'Address not found!';
                    this.locationChosen = false;
                }
            );
    }
}

@Component({
    selector: 'jhi-location-popup',
    template: ''
})
export class LocationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private locationPopupService: LocationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.locationPopupService
                    .open(LocationDialogComponent as Component, params['id']);
            } else {
                this.locationPopupService
                    .open(LocationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
