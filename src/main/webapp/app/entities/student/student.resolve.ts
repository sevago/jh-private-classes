import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';

import { Student } from './student.model';
import { StudentService } from './student.service';
import { Observable } from 'rxjs/Observable';
import { Account, Principal } from '../../shared';
import { LoginModalService } from '../../shared/login/login-modal.service';

@Injectable()
export class StudentResolve implements Resolve<any> {
    account: any;

    constructor(
        private router: Router,
        private loginModalService: LoginModalService,
        private studentService: StudentService,
        private principal: Principal
    ) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        this.principal.identity()
            .then((account) => {
                this.account = account;
            });

        return this.studentService.find(Number(route.paramMap.get('id')))
            .filter((student: Student) => {
                if (this.account.authorities.includes('ROLE_ADMIN') || this.account.id === student.userId) {
                    return true;
                } else {
                    this.router.navigate(['accessdenied']);
                }
                return false;
            });
    }
}
