/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PrivateclassesTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StudentDetailComponent } from '../../../../../../main/webapp/app/entities/student/student-detail.component';
import { StudentService } from '../../../../../../main/webapp/app/entities/student/student.service';
import { Student } from '../../../../../../main/webapp/app/entities/student/student.model';

describe('Component Tests', () => {

    describe('Student Management Detail Component', () => {
        let comp: StudentDetailComponent;
        let fixture: ComponentFixture<StudentDetailComponent>;
        let service: StudentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrivateclassesTestModule],
                declarations: [StudentDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: {
                            data: Observable.of({
                                student: new Student(10)
                            })
                        }
                    },
                    StudentService,
                    JhiEventManager
                ]
            }).overrideTemplate(StudentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StudentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StudentService);
        });

        describe('OnInit', () => {
            it('Should get student from the route data', () => {
            // GIVEN

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(comp.student).toEqual(jasmine.objectContaining({id: 10}));
            });
        });

        describe('Load', () => {
            it('Should call find on the service', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Student(10)));

                // WHEN
                comp.ngOnInit();
                comp.load(comp.student.id);

                // THEN
                expect(service.find).toHaveBeenCalledWith(10);
                expect(comp.student).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
