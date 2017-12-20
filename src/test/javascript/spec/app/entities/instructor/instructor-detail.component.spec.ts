/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PrivateclassesTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { InstructorDetailComponent } from '../../../../../../main/webapp/app/entities/instructor/instructor-detail.component';
import { InstructorService } from '../../../../../../main/webapp/app/entities/instructor/instructor.service';
import { Instructor } from '../../../../../../main/webapp/app/entities/instructor/instructor.model';

describe('Component Tests', () => {

    describe('Instructor Management Detail Component', () => {
        let comp: InstructorDetailComponent;
        let fixture: ComponentFixture<InstructorDetailComponent>;
        let service: InstructorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrivateclassesTestModule],
                declarations: [InstructorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: {
                            data: Observable.of({
                                instructor: new Instructor(10)
                            })
                        }
                    },
                    InstructorService,
                    JhiEventManager
                ]
            }).overrideTemplate(InstructorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InstructorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InstructorService);
        });

        describe('OnInit', () => {
            it('Should get instructor from the route data', () => {
            // GIVEN

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(comp.instructor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });

        describe('Load', () => {
            it('Should call find on the service', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Instructor(10)));

                // WHEN
                comp.ngOnInit();
                comp.load(comp.instructor.id);

                // THEN
                expect(service.find).toHaveBeenCalledWith(10);
                expect(comp.instructor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
