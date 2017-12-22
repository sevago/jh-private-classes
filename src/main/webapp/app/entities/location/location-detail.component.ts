import {Component, OnInit, OnDestroy, NgZone} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Location } from './location.model';
import { LocationService } from './location.service';
import { GMapsService } from '../../services/google-maps.service';

@Component({
    selector: 'jhi-location-detail',
    templateUrl: './location-detail.component.html'
})
export class LocationDetailComponent implements OnInit, OnDestroy {

    location: Location;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    latitude: number;
    longitude: number;
    locationChosen = false;
    geocodingError: any;

    constructor(
        private eventManager: JhiEventManager,
        private locationService: LocationService,
        private route: ActivatedRoute,
        private gMapsService: GMapsService,
        private __zone: NgZone
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.data.subscribe((data: {location: Location}) => {
            this.location = data.location;
            this.showLocationOnMap();
        });
        this.registerChangeInLocations();
    }

    load(id) {
        this.locationService.find(id).subscribe((location) => {
            this.location = location;
            this.showLocationOnMap();
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLocations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'locationListModification',
            (response) => this.load(this.location.id)
        );
    }

    showLocationOnMap() {
        if (this.location.address) {
            this.gMapsService.getLatLan(this.location.address)
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
}
