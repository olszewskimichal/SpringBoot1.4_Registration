package com.register.example.tasks;

import org.junit.Test;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class VerificationTokenRemoveTasksTest {

    @Test
    public void shouldExecuteTriggerNextDay(){
        org.springframework.scheduling.support.CronTrigger trigger = new CronTrigger("00 39 18 * * *");
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEEE");
        final Date yesterday = today.getTime();
        Date nextExecutionTime = trigger.nextExecutionTime(new TriggerContext(){

            @Override
            public Date lastScheduledExecutionTime() {
                return yesterday;
            }

            @Override
            public Date lastActualExecutionTime() {
                return yesterday;
            }

            @Override
            public Date lastCompletionTime() {
                return yesterday;
            }});

        Calendar cal = Calendar.getInstance();
        cal.setTime(nextExecutionTime);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        assertThat(hours).isEqualTo(18);
        assertThat(minutes).isEqualTo(39);
    }



}