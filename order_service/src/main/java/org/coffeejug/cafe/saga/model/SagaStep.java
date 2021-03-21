package org.coffeejug.cafe.saga.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "saga")
@Entity
public class SagaStep {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "aggregate_type")
    private String aggregateType;

    @Column(name = "aggregate_id")
    private String aggregateId;

    @Column(name = "payload")
    private String payload;

    @Column(name = "target")
    private String target;

    @Column(name = "type")
    private String type;

}
