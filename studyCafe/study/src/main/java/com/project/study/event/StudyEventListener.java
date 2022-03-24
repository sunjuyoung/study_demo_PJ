package com.project.study.event;

import com.project.study.config.AppProperties;
import com.project.study.domain.Account;
import com.project.study.domain.Notification;
import com.project.study.domain.NotificationType;
import com.project.study.domain.Study;
import com.project.study.mail.EmailMessage;
import com.project.study.mail.EmailService;
import com.project.study.repository.AccountPredicates;
import com.project.study.repository.AccountRepository;
import com.project.study.repository.NotificationRepository;
import com.project.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class StudyEventListener {

    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleStudyCreatedEvent(StudyCreatedEvent studyCreatedEvent){
        Study study = studyRepository.findStudyWithZonesAndTagsById(studyCreatedEvent.getStudy().getId());

        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTagsAndZones(study.getTags(), study.getZones()));
        accounts.forEach(account -> {
            if(account.isStudyCreatedByEmail()){
                //이메일 전송
                Context context = new Context();
                context.setVariable("nickname",account.getNickname());
                context.setVariable("link","/study/"+ URLEncoder.encode(study.getPath(), StandardCharsets.UTF_8));
                context.setVariable("linkName",study.getTitle());
                context.setVariable("message","새로운 스터디가 생겼습니다.");
                context.setVariable("host",appProperties.getHost());
                String message = templateEngine.process("email/simple-link",context);


                EmailMessage emailMessage = EmailMessage.builder()
                        .subject("스터디" + study.getTitle())
                        .to(account.getEmail())
                        .message(message)
                        .build();
                emailService.sendEmail(emailMessage);
            }
            if(account.isStudyCreatedByWeb()){
                //알림 전송
                Notification notification = Notification.builder()
                        .title(study.getTitle())
                        .link("/study/"+URLEncoder.encode(study.getPath(), StandardCharsets.UTF_8))
                        .checked(false)
                        .createdLocalDateTime(LocalDateTime.now())
                        .message(study.getShortDescription())
                        .account(account)
                        .notificationType(NotificationType.STUDY_CREATED)
                        .build();
                notificationRepository.save(notification);

            }
        });


    }
}
