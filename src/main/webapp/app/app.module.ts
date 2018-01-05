import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { PrivateclassesSharedModule, UserRouteAccessService } from './shared';
import { PrivateclassesAppRoutingModule} from './app-routing.module';
import { PrivateclassesHomeModule } from './home/home.module';
import { PrivateclassesCalendarModule } from './calendar/calendar.module';
import { PrivateclassesAdminModule } from './admin/admin.module';
import { PrivateclassesAccountModule } from './account/account.module';
import { PrivateclassesEntityModule } from './entities/entity.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        PrivateclassesAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        PrivateclassesSharedModule,
        PrivateclassesHomeModule,
        PrivateclassesAdminModule,
        PrivateclassesAccountModule,
        PrivateclassesEntityModule,
        PrivateclassesCalendarModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class PrivateclassesAppModule {}
