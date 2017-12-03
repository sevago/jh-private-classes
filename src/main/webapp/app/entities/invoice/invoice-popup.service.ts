import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Invoice } from './invoice.model';
import { InvoiceService } from './invoice.service';

@Injectable()
export class InvoicePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private invoiceService: InvoiceService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.invoiceService.find(id).subscribe((invoice) => {
                    if (invoice.periodStartDate) {
                        invoice.periodStartDate = {
                            year: invoice.periodStartDate.getFullYear(),
                            month: invoice.periodStartDate.getMonth() + 1,
                            day: invoice.periodStartDate.getDate()
                        };
                    }
                    if (invoice.periodEndDate) {
                        invoice.periodEndDate = {
                            year: invoice.periodEndDate.getFullYear(),
                            month: invoice.periodEndDate.getMonth() + 1,
                            day: invoice.periodEndDate.getDate()
                        };
                    }
                    if (invoice.issueDate) {
                        invoice.issueDate = {
                            year: invoice.issueDate.getFullYear(),
                            month: invoice.issueDate.getMonth() + 1,
                            day: invoice.issueDate.getDate()
                        };
                    }
                    if (invoice.dueDate) {
                        invoice.dueDate = {
                            year: invoice.dueDate.getFullYear(),
                            month: invoice.dueDate.getMonth() + 1,
                            day: invoice.dueDate.getDate()
                        };
                    }
                    this.ngbModalRef = this.invoiceModalRef(component, invoice);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.invoiceModalRef(component, new Invoice());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    invoiceModalRef(component: Component, invoice: Invoice): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.invoice = invoice;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
