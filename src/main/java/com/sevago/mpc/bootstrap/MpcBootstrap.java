package com.sevago.mpc.bootstrap;

import com.sevago.mpc.domain.*;
import com.sevago.mpc.domain.enumeration.RateCurrency;
import com.sevago.mpc.domain.enumeration.RateUnit;
import com.sevago.mpc.repository.*;
import com.sevago.mpc.security.AuthoritiesConstants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class MpcBootstrap implements CommandLineRunner {

    private final ActivityRepository activityRepository;
    private final InstructorRepository instructorRepository;
    private final InvoiceRepository invoiceRepository;
    private final LessonRepository lessonRepository;
    private final LessonTypeRepository lessonTypeRepository;
    private final LocationRepository locationRepository;
    private final RateRepository rateRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MpcBootstrap(ActivityRepository activityRepository, InstructorRepository instructorRepository, InvoiceRepository invoiceRepository, LessonRepository lessonRepository, LessonTypeRepository lessonTypeRepository, LocationRepository locationRepository, RateRepository rateRepository, StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.activityRepository = activityRepository;
        this.instructorRepository = instructorRepository;
        this.invoiceRepository = invoiceRepository;
        this.lessonRepository = lessonRepository;
        this.lessonTypeRepository = lessonTypeRepository;
        this.locationRepository = locationRepository;
        this.rateRepository = rateRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... strings) throws Exception {
        if(isDataStoreEmpty()){
            initData();
        }
    }

    private boolean isDataStoreEmpty() {
        return instructorRepository.count() == 0;
    }

    private void initData() {
        User firstUser = createUsers("Donald", "Duck", "donald@gmail.com");
        Activity firstActivity = createActivity("Figure Skating", firstUser);
        Activity secondActivity = createActivity("Power Skating", firstUser);
        Location firstLocation = createLocation("Planet Ice", "San Francisco", firstUser);
        Location secondLocation = createLocation("Olympic Oval", "Los Angeles", firstUser);
        LessonType firstLessonType = createLessonType("Private", BigDecimal.valueOf(1), firstUser);
        LessonType secondLessonType = createLessonType("Semi-Private", BigDecimal.valueOf(0.5), firstUser);
        Rate firstRate = createRate("$45 USD/hour", BigDecimal.valueOf(45), RateCurrency.USD, RateUnit.HOUR, firstUser);
        Rate secondRate = createRate("$270 USD/day", BigDecimal.valueOf(270), RateCurrency.USD, RateUnit.DAY, firstUser);
        Set<Rate> rates = new HashSet<>(Arrays.asList(firstRate,secondRate));
        Instructor firstInstructor = createInstructor("Donald", "San Francisco", "111-222-3333", "donald@google.com", rates, firstUser);
        Student firstStudent = createStudent("Tom", "111-222-3333", "tom@gmail.com", firstUser);
        Student secondStudent = createStudent("Jerry", "333-444-5555", "jerry@gmail.com", firstUser);
        Set<Student> studentsForFirstLesson = new HashSet<>(Arrays.asList(firstStudent,secondStudent));
        Set<Student> studentsForSecondLesson = new HashSet<>(Arrays.asList(firstStudent));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate firstLocalDate = LocalDate.parse("7/11/2017", formatter);
        LocalDate secondLocalDate = LocalDate.parse("15/11/2017", formatter);
        Lesson firstLesson = createLesson(firstLocalDate, BigDecimal.valueOf(1), BigDecimal.valueOf(45), firstActivity, firstLocation, secondLessonType, firstRate, firstInstructor, studentsForFirstLesson);
        Lesson secondLesson = createLesson(secondLocalDate, BigDecimal.valueOf(1), BigDecimal.valueOf(270), firstActivity, secondLocation, firstLessonType, secondRate, firstInstructor, studentsForSecondLesson);
        LocalDate startLocalDate = LocalDate.parse("1/11/2017", formatter);
        LocalDate endLocalDate = LocalDate.parse("30/11/2017", formatter);
        LocalDate issueLocalDate = LocalDate.parse("1/12/2017", formatter);
        LocalDate dueLocalDate = LocalDate.parse("7/11/2017", formatter);
        Set<Lesson> lessonsForFirstInvoice = new HashSet<>(Arrays.asList(firstLesson, secondLesson));
        Set<Lesson> lessonsForSecondInvoice = new HashSet<>(Arrays.asList(firstLesson));
        Invoice firstInvoice = createInvoice(1, startLocalDate, endLocalDate, issueLocalDate, dueLocalDate, BigDecimal.valueOf(45/2+270), RateCurrency.CAD, firstInstructor, firstStudent, lessonsForFirstInvoice);
        Invoice secondInvoice = createInvoice(2, startLocalDate, endLocalDate, issueLocalDate, dueLocalDate, BigDecimal.valueOf(45/2), RateCurrency.CAD, firstInstructor, secondStudent, lessonsForSecondInvoice);
    }

    private Invoice createInvoice(Integer number,
                                  LocalDate startDate,
                                  LocalDate endDate,
                                  LocalDate issueDate,
                                  LocalDate dueDate,
                                  BigDecimal amount,
                                  RateCurrency currency,
                                  Instructor instructor,
                                  Student student,
                                  Set<Lesson> lessons) {
        Invoice invoice = new Invoice();
        invoice.setNumber(number);
        invoice.setPeriodStartDate(startDate);
        invoice.setPeriodEndDate(endDate);
        invoice.setIssueDate(issueDate);
        invoice.setDueDate(dueDate);
        invoice.setTotalAmount(amount);
        invoice.setTotalCurrency(currency);
        invoice.setTeachingInstructor(instructor);
        invoice.setBillToStudent(student);
        invoice.setLessons(lessons);
        invoiceRepository.save(invoice);
        return invoice;
    }

    private Lesson createLesson(LocalDate date,
                                BigDecimal duration,
                                BigDecimal totalCharge,
                                Activity activity,
                                Location location,
                                LessonType lessonType,
                                Rate rate,
                                Instructor instructor,
                                Set<Student> students) {
        Lesson lesson = new Lesson();
        lesson.setDate(date);
        lesson.setDuration(duration);
        lesson.setTotalCharge(totalCharge);
        lesson.setActivity(activity);
        lesson.setLocation(location);
        lesson.setLessonType(lessonType);
        lesson.setRate(rate);
        lesson.setTeachingInstructor(instructor);
        lesson.setStudents(students);
        lessonRepository.save(lesson);
        return lesson;
    }

    private Student createStudent(String name, String phoneNumber, String email, User user ) {
        Student student = new Student();
        student.setName(name);;
        student.setPhoneNumber(phoneNumber);
        student.setEmail(email);
        student.setUser(user);
        studentRepository.save(student);
        return student;
    }

    private Instructor createInstructor(String name, String address, String phoneNumber, String email, Set<Rate> rates, User user) {
        Instructor instructor = new Instructor();
        instructor.setName(name);
        instructor.setAddress(address);
        instructor.setPhoneNumber(phoneNumber);
        instructor.setEmail(email);
        instructor.setRates(rates);
        instructor.setUser(user);
        instructorRepository.save(instructor);
        return instructor;
    }


    private User createUsers(String firstName, String lastName, String login) {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode("secret"));
        user.setLogin(login);
        user.setEmail(login);
        user.setLangKey("en");
        user.setActivated(true);
        user.setAuthorities(authorities);
        userRepository.save(user);
        return user;
    }

    private Activity createActivity(String name, User user) {
        Activity activity = new Activity();
        activity.setName(name);
        activity.setDescription(name);
        activity.setUser(user);
        activityRepository.save(activity);
        return activity;
    }

    private Location createLocation(String name, String address, User user) {
        Location location = new Location();
        location.setName(name);
        location.setDescription(name);
        location.setAddress(address);
        location.setUser(user);
        locationRepository.save(location);
        return location;
    }

    private LessonType createLessonType(String desc, BigDecimal ratio, User user) {
        LessonType lessonType = new LessonType();
        lessonType.setDescription(desc);
        lessonType.setRatio(ratio);
        lessonType.setUser(user);
        lessonTypeRepository.save(lessonType);
        return lessonType;
    }

    private Rate createRate(String desc, BigDecimal amount, RateCurrency currency, RateUnit unit, User user) {
        Rate rate = new Rate();
        rate.setDescription(desc);
        rate.setAmount(amount);
        rate.setCurrency(currency);
        rate.setUnit(unit);
        rate.setUser(user);
        rateRepository.save(rate);
        return rate;
    }
}
