/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { PrivateclassesTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LessonTypeDetailComponent } from '../../../../../../main/webapp/app/entities/lesson-type/lesson-type-detail.component';
import { LessonTypeService } from '../../../../../../main/webapp/app/entities/lesson-type/lesson-type.service';
import { LessonType } from '../../../../../../main/webapp/app/entities/lesson-type/lesson-type.model';

describe('Component Tests', () => {

    describe('LessonType Management Detail Component', () => {
        let comp: LessonTypeDetailComponent;
        let fixture: ComponentFixture<LessonTypeDetailComponent>;
        let service: LessonTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PrivateclassesTestModule],
                declarations: [LessonTypeDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LessonTypeService,
                    JhiEventManager
                ]
            }).overrideTemplate(LessonTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LessonTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LessonTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new LessonType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.lessonType).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
