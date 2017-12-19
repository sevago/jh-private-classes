import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Activity } from './activity.model';
import { ActivityService } from './activity.service';
import { Observable } from 'rxjs/Observable';
import { Account, Principal } from '../../shared';
import { LoginModalService } from '../../shared/login/login-modal.service';

@Injectable()
export class ActivityResolve implements Resolve<any> {
    account: any;

    constructor(
        private router: Router,
        private loginModalService: LoginModalService,
        private activityService: ActivityService,
        private principal: Principal
    ) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        this.principal.identity()
            .then((account) => {
                this.account = account;
            });

        return this.activityService.find(Number(route.paramMap.get('id')))
            .filter((activity: Activity) => {
                if (this.account.authorities.includes('ROLE_ADMIN') || this.account.id === activity.userId) {
                    return true;
                } else {
                    this.router.navigate(['accessdenied']);
                }
                return false;
            });
    }
}
