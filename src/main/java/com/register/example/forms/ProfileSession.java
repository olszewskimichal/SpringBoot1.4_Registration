package com.register.example.forms;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProfileSession implements Serializable {
    transient String email;
    transient List<Cokolwiek> cokolwiek = new ArrayList<>();

    public void saveForm(ProfileForm profileForm) {
        this.email = profileForm.getEmail();
        this.cokolwiek = profileForm.getForms();
    }

    public ProfileForm toForm() {
        ProfileForm profileForm = new ProfileForm();
        profileForm.setEmail(email);
        profileForm.setForms(cokolwiek);
        return profileForm;
    }

    public List<Cokolwiek> getCokolwiek() {
        return cokolwiek;
    }

    public void setCokolwiek(List<Cokolwiek> cokolwiek) {
        this.cokolwiek = cokolwiek;
    }

    @Override
    public String toString() {
        return "ProfileSession{" +
                "cokolwiek=" + cokolwiek +
                ", email='" + email + '\'' +
                '}';
    }
}
