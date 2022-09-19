package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.openmarkov.OpenMarkovClassifier;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualComparisonResponse;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualRequest;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualResponse;
import org.openmarkov.core.exception.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Server {

    @Value ("${modelpath}")
    private String path;
    @Value ("${model}")
    private String model;
    private OpenMarkovClassifier classifier;

    public Server() {
    }

    public Server(OpenMarkovClassifier classifier) {
        this.classifier = classifier;
    }

    @PostMapping ("classifyIndividualBayesian")
    public ClassifyIndividualResponse classifyIndividualBayesian(@RequestBody ClassifyIndividualRequest req)
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        if (classifier == null) {
            classifier = new OpenMarkovClassifier(path, model);
        }
        return classifier.classify(req.getEvidence(), req.getTargets());
    }

    @PostMapping ("classifyIndividualWithComparisons")
    public ClassifyIndividualComparisonResponse classifyIndividualWithComparisons(
            @RequestBody ClassifyIndividualRequest req)
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        if (classifier == null) {
            classifier = new OpenMarkovClassifier(path, model);
        }
        return classifier.compareClassifications(req.getEvidence(), req.getTargets());
    }

    @GetMapping ("getBayesianModel")
    public String getBayesianModel()
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        if (classifier == null) {
            classifier = new OpenMarkovClassifier(path, model);
        }
        return classifier.getModelPgmx();
    }
}
