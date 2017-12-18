import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Location } from './location.model';
import { LocationService } from './location.service';
import { Observable } from 'rxjs/Observable';
import { Account, Principal } from '../../shared';
import { LoginModalService } from '../../shared/login/login-modal.service';

@Injectable()
export class LocationResolve implements Resolve<any> {
    account: any;

    constructor(
        private router: Router,
        private loginModalService: LoginModalService,
        private locationService: LocationService,
        private principal: Principal
    ) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        this.principal.identity()
            .then((account) => {
                this.account = account;
            });

        return this.locationService.find(Number(route.paramMap.get('id')))
            .filter((location: Location) => {
                if (this.account.authorities.includes('ROLE_ADMIN') || this.account.id === location.userId) {
                    return true;
                } else {
                    this.router.navigate(['accessdenied']);
                }
                return false;
            });
    }
}
