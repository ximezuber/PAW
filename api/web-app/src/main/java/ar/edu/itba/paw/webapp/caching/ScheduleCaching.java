package ar.edu.itba.paw.webapp.caching;

import ar.edu.itba.paw.interfaces.web.Caching;
import ar.edu.itba.paw.webapp.dto.ScheduleDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ScheduleCaching implements Caching<ScheduleDto> {

    @Override
    public int calculateHash(ScheduleDto schedule) {
        if(schedule == null) {
            return 0;
        }
        return Objects.hash(schedule.getDay(), schedule.getHour());
    }
}
