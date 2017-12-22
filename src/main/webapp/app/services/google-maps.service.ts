import { Injectable, NgZone } from '@angular/core';
import { GoogleMapsAPIWrapper } from '@agm/core';
import { MapsAPILoader } from '@agm/core';
import { Observable, Observer } from 'rxjs';

declare var google: any;

@Injectable()
export class GMapsService extends GoogleMapsAPIWrapper {
    constructor(
        private __loader: MapsAPILoader,
        private __zone: NgZone
    ) {
        super(__loader, __zone);
    }

    getLatLan(address: string): Observable<any> {
        return Observable.create((observer) => {
            try {
                this.__loader.load().then(() => {
                    const geocoder = new google.maps.Geocoder();
                    geocoder.geocode({ address }, (results, status) => {
                        if (status === google.maps.GeocoderStatus.OK) {
                            const place = results[0].geometry.location;
                            observer.next(place);
                            observer.complete();
                        } else {
                            if (status === google.maps.GeocoderStatus.ZERO_RESULTS) {
                                observer.error('Address not found!');
                            }else {
                                observer.error(status);
                            }
                            observer.complete();
                        }
                    });
                });
            } catch (error) {
                observer.error('error getGeocoding' + error);
                observer.complete();
            }
        });
    }
}
