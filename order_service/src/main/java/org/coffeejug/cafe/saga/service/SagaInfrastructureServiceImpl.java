package org.coffeejug.cafe.saga.service;

import lombok.RequiredArgsConstructor;
import org.coffeejug.cafe.saga.model.SagaStep;
import org.coffeejug.cafe.saga.repository.SagaStepRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SagaInfrastructureServiceImpl implements SagaInfrastructureService {

    private final SagaStepRepository sagaStepRepository;

    @Override
    public void saveStep(SagaStep sagaStep) {
        sagaStepRepository.saveAndFlush(sagaStep);
    }
}
