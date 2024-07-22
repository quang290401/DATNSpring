    package com.example.datn.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;
    import java.util.UUID;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @MappedSuperclass
    public class SuperEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "createDate")
        private LocalDate createDate;

        @Column(name = "updateDate")
        private LocalDate updateDate;

        @PrePersist
        protected void onCreate() {
            this.createDate = LocalDate.now();
        }

        @PreUpdate
        protected void onUpdate() {
            this.updateDate = LocalDate.now();
        }
    }