package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ScheduleDao;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.model.Schedule;
import ar.edu.itba.paw.model.ScheduleKey;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Schedule createSchedule(int day, int hour, DoctorClinic doctorClinic){
        doctorClinic = entityManager.merge(doctorClinic);
        Schedule schedule = new Schedule(day, hour, doctorClinic);
        entityManager.persist(schedule);
        return schedule;
    }

    @Override
    public List<Schedule> getDoctorClinicSchedule(DoctorClinic doctorClinic) {
        final TypedQuery<Schedule> query = entityManager.createQuery(
                "FROM Schedule AS schedule WHERE schedule.doctorClinic.doctor.license = :doctor " +
                "AND schedule.doctorClinic.clinic.id = :clinic ORDER BY schedule.scheduleKey.hour, " +
                        "schedule.scheduleKey.day", Schedule.class);

        query.setParameter("doctor",doctorClinic.getDoctor().getLicense());
        query.setParameter("clinic",doctorClinic.getClinic().getId());

        return query.getResultList();
    }

    @Override
    public List<Schedule> getDoctorsSchedule(Doctor doctor) {
        final TypedQuery<Schedule> query = entityManager.createQuery(
                "FROM Schedule AS schedule WHERE schedule.doctorClinic.doctor.license = :doctor " +
                        "ORDER BY schedule.scheduleKey.hour, " +
                        "schedule.scheduleKey.day", Schedule.class);
        query.setParameter("doctor", doctor.getLicense());

        return query.getResultList();
    }

    @Override
    public Optional<Schedule> getDoctorScheduledHour(Doctor doctor, int day, int hour) {
        Schedule schedule = entityManager.find(Schedule.class, new ScheduleKey(day, hour, doctor.getLicense()));
        return Optional.ofNullable(schedule);
    }

    @Override
    public void deleteSchedule(Schedule schedule) {
        Schedule contextSchedule = entityManager.merge(schedule);
        entityManager.remove(contextSchedule);
    }
}
