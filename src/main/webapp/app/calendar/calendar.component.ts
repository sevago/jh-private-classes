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
export class CalendarComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
