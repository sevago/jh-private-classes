import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Rate } from './rate.model';
import { RateService } from './rate.service';
import { Observable } from 'rxjs/Observable';
import { Account, Principal } from '../../shared';
import { LoginModalService } from '../../shared/login/login-modal.service';

@Injectable()
export class RateResolve implements Resolve<any> {
    account: any;

    constructor(
        private router: Router,
        private loginModalService: LoginModalService,
        private rateService: RateService,
        private principal: Principal
    ) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        this.principal.identity()
            .then((account) => {
                this.account = account;
            });

        return this.rateService.find(Number(route.paramMap.get('id')))
            .filter((rate: Rate) => {
                if (this.account.authorities.includes('ROLE_ADMIN') || this.account.id === rate.userId) {
                    return true;
                } else {
                    this.router.navigate(['accessdenied']);
                }
                return false;
            });
    }
}
