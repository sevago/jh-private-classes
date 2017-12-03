import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Invoice } from './invoice.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class InvoiceService {

    private resourceUrl = SERVER_API_URL + 'api/invoices';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/invoices';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(invoice: Invoice): Observable<Invoice> {
        const copy = this.convert(invoice);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(invoice: Invoice): Observable<Invoice> {
        const copy = this.convert(invoice);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Invoice> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Invoice.
     */
    private convertItemFromServer(json: any): Invoice {
        const entity: Invoice = Object.assign(new Invoice(), json);
        entity.periodStartDate = this.dateUtils
            .convertLocalDateFromServer(json.periodStartDate);
        entity.periodEndDate = this.dateUtils
            .convertLocalDateFromServer(json.periodEndDate);
        entity.issueDate = this.dateUtils
            .convertLocalDateFromServer(json.issueDate);
        entity.dueDate = this.dateUtils
            .convertLocalDateFromServer(json.dueDate);
        return entity;
    }

    /**
     * Convert a Invoice to a JSON which can be sent to the server.
     */
    private convert(invoice: Invoice): Invoice {
        const copy: Invoice = Object.assign({}, invoice);
        copy.periodStartDate = this.dateUtils
            .convertLocalDateToServer(invoice.periodStartDate);
        copy.periodEndDate = this.dateUtils
            .convertLocalDateToServer(invoice.periodEndDate);
        copy.issueDate = this.dateUtils
            .convertLocalDateToServer(invoice.issueDate);
        copy.dueDate = this.dateUtils
            .convertLocalDateToServer(invoice.dueDate);
        return copy;
    }
}
