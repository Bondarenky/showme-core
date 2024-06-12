package com.itzroma.showme.email.event;

import com.itzroma.showme.domain.entity.VerificationToken;
import com.itzroma.showme.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class EmailVerificationEvent extends ApplicationEvent {
    private final User user;
    private final String url;
    private final VerificationToken verificationToken;

    public EmailVerificationEvent(User user, String url, VerificationToken verificationToken) {
        super(user);
        this.user = user;
        this.url = url;
        this.verificationToken = verificationToken;
    }
}
