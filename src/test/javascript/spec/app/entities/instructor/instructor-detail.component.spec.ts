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
                        useValue: new MockActivatedRoute({id: 123})
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
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Instructor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.instructor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
