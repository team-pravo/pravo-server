package com.pravo.pravo.domain.member.model;

import com.pravo.pravo.global.common.model.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "member")
@SQLRestriction("deleted = false")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String profileImageUrl;
    private String socialId;
    private Long point;
    private String fcmToken = "";

    @Column(nullable = false)
    private boolean deleted = false;

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    protected Member() {
    }

    public Member(String name, String socialId) {
        this.name = name;
        this.socialId = socialId;
        this.point = 0L;
    }

    public String getSocialId() {
        return this.socialId;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public Long getPoint() { return this.point; }

    public String getFcmToken() {
        return this.fcmToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updatePoint(Long point) {
        this.point += point;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
