package com.carrier.modelexposer.classifier;

import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;

import java.util.Map;

public abstract class Classifier {
    public abstract RiskResponse classify(Map<String, String> evidence) throws Exception;

    public abstract ReducedRiskResponse compareClassifications(Map<String, String> evidence,
                                                               Map<String, String> comparisons) throws Exception;

}
