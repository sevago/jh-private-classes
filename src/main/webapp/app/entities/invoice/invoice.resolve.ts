import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Invoice } from './invoice.model';
import { InvoiceService } from './invoice.service';
import { Observable } from 'rxjs/Observable';
import { Account, Principal } from '../../shared';
import { LoginModalService } from '../../shared/login/login-modal.service';

@Injectable()
export class InvoiceResolve implements Resolve<any> {
    account: any;

    constructor(
        private router: Router,
        private loginModalService: LoginModalService,
        private invoiceService: InvoiceService,
        private principal: Principal
    ) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        this.principal.identity()
            .then((account) => {
                this.account = account;
            });

        return this.invoiceService.find(Number(route.paramMap.get('id')))
            .filter((invoice: Invoice) => {
                if (this.account.authorities.includes('ROLE_ADMIN') || this.account.id) {
                    return true;
                } else {
                    this.router.navigate(['accessdenied']);
                }
                return false;
            });
    }
}
