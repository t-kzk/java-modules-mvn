package org.kzk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "files", schema = "module_2_4_servlet")
public class FileE {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "file_path")
    private String filePath;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileE file = (FileE) o;
        return getId() != null && Objects.equals(getId(), file.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
