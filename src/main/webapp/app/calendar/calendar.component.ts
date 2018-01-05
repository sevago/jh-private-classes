import { Component, ChangeDetectionStrategy, OnInit, OnDestroy } from '@angular/core';

import {
    startOfDay,
    endOfDay,
    subDays,
    addDays,
    endOfMonth,
    isSameDay,
    isSameMonth,
    addHours,
    addMinutes,
    format,
    startOfMonth,
    getDaysInMonth
} from 'date-fns';

import { Subject } from 'rxjs/Subject';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import {
    CalendarEvent,
    CalendarEventAction,
    CalendarEventTimesChangedEvent,
    CalendarMonthViewDay
} from 'angular-calendar';

import { Principal } from '../shared';
import { JhiEventManager } from 'ng-jhipster';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { LessonService } from '../entities/lesson';

const colors: any = {
    red: {
        primary: '#ad2121',
        secondary: '#FAE3E3'
    },
    blue: {
        primary: '#1e90ff',
        secondary: '#D1E8FF'
    },
    yellow: {
        primary: '#e3bc08',
        secondary: '#FDF1BA'
    },
    green: {
        primary: '#008000',
        secondary: '#C3FDB8'
    }
};

@Component({
    selector: 'jhi-calendar',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './calendar.component.html',
    styleUrls: ['calendar.component.css']
})
export class CalendarComponent implements OnInit, OnDestroy {
    modalRef: NgbModalRef;
    view = 'month';
    viewDate: Date = new Date();
    modalData: {
        action: string;
        event: CalendarEvent;
    };
    eventSubscriber: Subscription;
    actions: CalendarEventAction[] = [
        {
            label: '<i class="fa fa-fw fa-pencil"></i>',
            onClick: ({event}: { event: CalendarEvent }): void => {
                this.handleEvent('edit', event);
            }
        },
        {
            label: '<i class="fa fa-fw fa-times"></i>',
            onClick: ({event}: { event: CalendarEvent }): void => {
                this.handleEvent('delete', event);
            }
        }
    ];
    refresh: Subject<any> = new Subject();
    events: CalendarEvent[] = [];
    activeDayIsOpen = true;
    account: any;

    constructor(private principal: Principal,
                private router: Router,
                private eventManager: JhiEventManager,
                private lessonService: LessonService
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.populateCalendar();
        this.registerForChanges();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerForChanges() {
        this.eventSubscriber = this.eventManager.subscribe(
            'lessonListModification',
            (response) => this.reset());
    }

    reset() {
        this.events = [];
        this.populateCalendar();
    }

    viewDateChanged() {
        this.reset();
    }

    populateCalendar() {

    }

    beforeMonthViewRender({body}: { body: CalendarMonthViewDay[] }): void {
        body.forEach((cell) => {
            cell['dayPoints'] = cell.events.filter((e) => e.meta['entity'] === 'points');
            cell['weekPoints'] = cell.events.filter((e) => e.meta['entity'] === 'totalPoints');
        });
    }

    dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
        if (isSameMonth(date, this.viewDate)) {
            if (
                (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
                events.length === 0
            ) {
                this.activeDayIsOpen = false;
            } else {
                this.activeDayIsOpen = true;
                this.viewDate = date;
            }
        }
    }

    eventTimesChanged({event, newStart, newEnd}: CalendarEventTimesChangedEvent): void {
        event.start = newStart;
        event.end = newEnd;
        this.handleEvent('Dropped or resized', event);
        this.refresh.next();
    }

    handleEvent(action: string, event: CalendarEvent): void {
        action = (action === 'Clicked') ? 'edit' : action;
        this.modalData = {event, action};
        const url = this.router.createUrlTree(['/', {outlets: {popup: event.meta.entity + '/' + event.meta.id + '/' + action}}]);
        this.router.navigateByUrl(url.toString());
    }
}
