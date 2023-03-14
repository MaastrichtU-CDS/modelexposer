package com.carrier.modelexposer.classifier.score2;

import com.carrier.modelexposer.classifier.Classifier;
import com.carrier.modelexposer.exception.InvalidDoubleException;
import com.carrier.modelexposer.exception.InvalidIntegerException;
import com.carrier.modelexposer.exception.MissingAttributeException;
import com.carrier.modelexposer.exception.UnknownStateException;
import com.carrier.modelexposer.webservice.domain.ReducedRiskResponse;
import com.carrier.modelexposer.webservice.domain.RiskResponse;

import java.util.HashMap;
import java.util.Map;

import static com.carrier.modelexposer.util.Util.*;

public class Score2Classifier extends Classifier {

    public RiskResponse classify(Map<String, String> evidence)
            throws MissingAttributeException, InvalidIntegerException, InvalidDoubleException, UnknownStateException {
        RiskResponse response = createRiskResponse(score2(evidence));
        return response;
    }

    @Override
    public ReducedRiskResponse compareClassifications(Map<String, String> evidence,
                                                      Map<String, String> comparison)
            throws InvalidIntegerException, InvalidDoubleException,
                   MissingAttributeException, UnknownStateException {
        ReducedRiskResponse result = new ReducedRiskResponse();
        result.setBaseline(classify(evidence));

        Map<String, String> evidences = new HashMap<>();
        evidences.putAll(evidence);
        evidences.putAll(comparison);
        result.setResult(comparison, classify(evidences).getProbabilities());
        return result;
    }

    @SuppressWarnings ("checkstyle:magicNumber") //ignore magic numbers in the score-function
    private double score2(Map<String, String> evidence)
            throws MissingAttributeException, InvalidIntegerException, InvalidDoubleException, UnknownStateException {
        //Collect relevant attributes
        //Score function in the following DOI: 10.1093/eurheartj/ehab309
        int smokingYes = 0;
        String gender = getStringValue(evidence, "gender");
        int age = getIntValue(evidence, "age");
        String smoking = getStringValue(evidence, "current_smoker");
        double sbp = getDoubleValue(evidence, "SBP");
        double tc = getDoubleValue(evidence, "TC");
        double hdl = getDoubleValue(evidence, "HDL");

        if (smoking == "yes") {
            smokingYes = 1;
        } else if (smoking == "no") {
            smokingYes = 0;
        } else {
            throw new UnknownStateException(smoking, "current_smoker", "'yes', 'no'");
        }

        if (gender.equals("male")) {
            double lp =
                    0.3742 * (age - 60) / 5 + 0.6012 * smokingYes + 0.2777 * (sbp - 120) / 20 + 0.1458
                            * (tc - 6) + -0.2698 * (hdl - 1.3) * 2 + -0.0755 * (age - 60) / 5 * smokingYes + -0.0255
                            * (age - 60) / 5 * (sbp - 120) / 20 + -0.0281 * (age - 60) / 5 * (tc - 6) + 0.0426
                            * (age - 60) / 5 * (hdl - 1.3) * 2;
            double unscaledPred = 1 - Math.pow(0.9605, Math.exp(lp));

            return (1 - Math.exp(-1 * Math.exp(-0.5699 + 0.7476 * Math.log(-1 * Math.log(1 - unscaledPred)))));
        } else if (gender.equals("female")) {
            double lp =
                    0.4648 * (age - 60) / 5 + 0.7744 * smokingYes + 0.3131 * (sbp - 120) / 20 + 0.1002
                            * (tc - 6) + -0.2606 * (hdl - 1.3) * 2 + -0.1088 * (age - 60) / 5 * smokingYes + -0.0277
                            * (age - 60) / 5 * (sbp - 120) / 20 + -0.0226 * (age - 60) / 5 * (tc - 6) + 0.0613
                            * (age - 60) / 5 * (hdl - 1.3) * 2;
            double unscaledPred = 1 - Math.pow(0.9776, Math.exp(lp));

            return (1 - Math.exp(-1 * Math.exp(-0.7380 + 0.7019 * Math.log(-1 * Math.log(1 - unscaledPred)))));
        } else {
            throw new UnknownStateException(gender, "gender", "'male', 'female'");
        }
    }

    private RiskResponse createRiskResponse(double risk) {
        RiskResponse response = new RiskResponse();
        Map<String, Double> probabilities = new HashMap<>();
        probabilities.put("CVD", risk);
        response.setProbabilities(probabilities);
        return response;
    }
}
