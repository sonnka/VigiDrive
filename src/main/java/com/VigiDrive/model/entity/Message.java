package com.VigiDrive.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "text")
    private String text;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
