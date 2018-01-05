import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Lesson } from './lesson.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class LessonService {

    private resourceUrl = SERVER_API_URL + 'api/lessons';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/lessons';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(lesson: Lesson): Observable<Lesson> {
        const copy = this.convert(lesson);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(lesson: Lesson): Observable<Lesson> {
        const copy = this.convert(lesson);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Lesson> {
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

    byMonth(month: string): Observable<ResponseWrapper> {
        return this.http.get(`${this.resourceUrl}/by-month/${month}`)
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
     * Convert a returned JSON object to Lesson.
     */
    private convertItemFromServer(json: any): Lesson {
        const entity: Lesson = Object.assign(new Lesson(), json);
        entity.date = this.dateUtils
            .convertLocalDateFromServer(json.date);
        return entity;
    }

    /**
     * Convert a Lesson to a JSON which can be sent to the server.
     */
    private convert(lesson: Lesson): Lesson {
        const copy: Lesson = Object.assign({}, lesson);
        copy.date = this.dateUtils
            .convertLocalDateToServer(lesson.date);
        return copy;
    }
}
