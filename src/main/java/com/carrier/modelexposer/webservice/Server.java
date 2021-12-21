package com.carrier.modelexposer.webservice;

import com.carrier.modelexposer.webservice.domain.ClassifyIndividualRequest;
import com.carrier.modelexposer.webservice.domain.ClassifyIndividualResponse;
import com.carrier.modelexposer.webservice.openmarkov.OpenMarkovClassifier;
import org.openmarkov.core.exception.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class Server {

    @GetMapping ("getUniqueValues")
    public ClassifyIndividualResponse classifyIndividualBayesian(@RequestBody ClassifyIndividualRequest req)
            throws NodeNotFoundException, NotEvaluableNetworkException, IncompatibleEvidenceException,
                   InvalidStateException, UnexpectedInferenceException {
        OpenMarkovClassifier markov = new OpenMarkovClassifier();
        return markov.classify(req.getEvidence(), req.getTargets());
    }
}
