/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PrivateclassesTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RateDetailComponent } from '../../../../../../main/webapp/app/entities/rate/rate-detail.component';
import { RateService } from '../../../../../../main/webapp/app/entities/rate/rate.service';
import { Rate } from '../../../../../../main/webapp/app/entities/rate/rate.model';

describe('Component Tests', () => {

    describe('Rate Management Detail Component', () => {
        let comp: RateDetailComponent;
        let fixture: ComponentFixture<RateDetailComponent>;
        let service: RateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrivateclassesTestModule],
                declarations: [RateDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: {
                            data: Observable.of({
                                rate: new Rate(10)
                            })
                        }
                    },
                    RateService,
                    JhiEventManager
                ]
            }).overrideTemplate(RateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RateService);
        });

        describe('OnInit', () => {
            it('Should get rate from the route data', () => {
            // GIVEN

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(comp.rate).toEqual(jasmine.objectContaining({id: 10}));
            });
        });

        describe('Load', () => {
            it('Should call find on the service', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Rate(10)));

                // WHEN
                comp.ngOnInit();
                comp.load(comp.rate.id);

                // THEN
                expect(service.find).toHaveBeenCalledWith(10);
                expect(comp.rate).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
