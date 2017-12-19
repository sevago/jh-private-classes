/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PrivateclassesTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LocationDetailComponent } from '../../../../../../main/webapp/app/entities/location/location-detail.component';
import { LocationService } from '../../../../../../main/webapp/app/entities/location/location.service';
import { Location } from '../../../../../../main/webapp/app/entities/location/location.model';

describe('Component Tests', () => {

    describe('Location Management Detail Component', () => {
        let comp: LocationDetailComponent;
        let fixture: ComponentFixture<LocationDetailComponent>;
        let service: LocationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrivateclassesTestModule],
                declarations: [LocationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: {
                            data: Observable.of({
                                location: new Location(10)
                            })
                        }
                    },
                    LocationService,
                    JhiEventManager
                ]
            }).overrideTemplate(LocationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LocationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LocationService);
        });

        describe('OnInit', () => {
            it('Should get location from the route data', () => {
            // GIVEN

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(comp.location).toEqual(jasmine.objectContaining({id: 10}));
            });
        });

        describe('Load', () => {
            it('Should call find on the service', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Location(10)));

                // WHEN
                comp.ngOnInit();
                comp.load(comp.location.id);

                // THEN
                expect(service.find).toHaveBeenCalledWith(10);
                expect(comp.location).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
