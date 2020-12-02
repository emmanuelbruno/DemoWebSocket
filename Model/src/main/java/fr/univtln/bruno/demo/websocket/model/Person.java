package fr.univtln.bruno.demo.websocket.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person implements Serializable {
    @EqualsAndHashCode.Include
    @Builder.Default
    UUID id = UUID.randomUUID();

    String firstname;

    String lastname;
}
