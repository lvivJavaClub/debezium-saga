package org.coffeejug.cafe.saga.service;

import org.coffeejug.cafe.saga.model.SagaStep;

public interface SagaInfrastructureService {

    void saveStep(SagaStep sagaStep);

}
