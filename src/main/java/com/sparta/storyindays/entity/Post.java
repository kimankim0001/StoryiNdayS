package com.sparta.storyindays.entity;

import com.sparta.storyindays.dto.post.PostReqDto;
import com.sparta.storyindays.enums.post.PostType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class Post extends Timstamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "contents", nullable = false, length = 255)
    private String contents;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_type", nullable = false, length = 255)
    private PostType postType;

    @Column(name = "is_pinned", nullable = false)
    private Boolean isPinned;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany
//    private Comment comment;

    @Builder
    public Post(String title, String contents, boolean isPinned, PostType postType, User user) {
        this.title = title;
        this.contents = contents;
        this.isPinned = isPinned;
        this.postType = postType;
        this.user = user;
    }

    public void setPin() {
        isPinned = true;
    }

    public void update(PostReqDto reqDto) {
        title = reqDto.getTitle();
        contents = reqDto.getContents();
    }
}
