package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ScheduleDao;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.model.Schedule;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Schedule createSchedule(int day, int hour, DoctorClinic doctorClinic){
        Schedule schedule = new Schedule(day, hour, doctorClinic);
        entityManager.persist(schedule);
        return schedule;
    }

    @Override
    public List<Schedule> getDoctorClinicSchedule(DoctorClinic doctorClinic){
        final TypedQuery<Schedule> query = entityManager.createQuery(
                "from Schedule as schedule where schedule.doctorClinic.doctor.license = :doctor " +
                "and schedule.doctorClinic.clinic.id = :clinic ORDER BY schedule.scheduleKey.hour, " +
                        "schedule.scheduleKey.day",Schedule.class);

        query.setParameter("doctor",doctorClinic.getDoctor().getLicense());
        query.setParameter("clinic",doctorClinic.getClinic().getId());

        return query.getResultList();
    }

    @Override
    public List<Schedule> getDoctorsSchedule(Doctor doctor) {
        final TypedQuery<Schedule> query = entityManager.createQuery(
                "from Schedule as schedule where schedule.doctorClinic.doctor.license = :doctor " +
                        "ORDER BY schedule.scheduleKey.hour, " +
                        "schedule.scheduleKey.day",Schedule.class);

        query.setParameter("doctor", doctor.getLicense());

        return query.getResultList();
    }

    @Override
    public boolean doctorHasSchedule(Doctor doctor, int day, int hour) {
        final TypedQuery<Schedule> query = entityManager.createQuery("from Schedule as schedule " +
                "where schedule.doctorClinic.doctor.license = :doctor and schedule.scheduleKey.day = :day" +
                " and schedule.scheduleKey.hour = :hour", Schedule.class);

        query.setParameter("doctor", doctor.getLicense());
        query.setParameter("day", day);
        query.setParameter("hour", hour);
        List<Schedule> schedules = query.getResultList();
        return !schedules.isEmpty();
    }

    @Override
    public int deleteSchedule(int hour, int day, DoctorClinic doctorClinic){
        final Query query = entityManager.createQuery("delete from Schedule as schedule " +
                "where schedule.scheduleKey.day = :day and schedule.scheduleKey.hour = :hour " +
                "and schedule.scheduleKey.doctor = :doctor and schedule.clinic = :clinic");
        query.setParameter("day", day);
        query.setParameter("hour", hour);
        query.setParameter("doctor", doctorClinic.getDoctor().getLicense());
        query.setParameter("clinic", doctorClinic.getClinic().getId());
        return query.executeUpdate();
    }
}
