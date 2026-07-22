package com.fx.api.service;

import com.fx.api.model.IncomingBatch;
import com.fx.api.model.IncomingRate;
import com.fx.api.repo.RateRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FeedService.class);
    private final RateRepository rates;
    private final Orchestrator orchestrator;
    private final AcceptingState accepting;
    public FeedService(RateRepository rates, Orchestrator orchestrator, AcceptingState accepting) {
        this.rates = rates;
        this.orchestrator = orchestrator;
        this.accepting = accepting;
    }

    public void handle(IncomingBatch batch) {
        if (!accepting.isAccepting()) {
            log.info("Declined batch {} while accepting is off", batch.batchId());
            orchestrator.acknowledge(batch.batchId(), "DECLINED");
            return;
        }

        for (IncomingRate rate : batch.rates()) {
            rates.insert(rate.base(), rate.quote(), rate.rate());
        }
        log.info("Stored {} rates from batch {}", batch.rates().size(), batch.batchId());
        orchestrator.acknowledge(batch.batchId(), "ACCEPTED");
    }
}